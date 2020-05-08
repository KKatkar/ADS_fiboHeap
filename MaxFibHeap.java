
import java.util.*;


public class MaxFibHeap
{

    FiboNode maxNode;
    int numNodes;

 
    public void insert(FiboNode node) // Inserts new node to the max Fibonacci Heap
    {
        //check if max node exists
        if (maxNode != null) { 
            node.right = maxNode.right; //append between the right sibling of max and maxNode
            maxNode.right = node;
            node.left = maxNode;

            if ( node.right!=null) {     
                node.right.left = node; // if node exists to the right update the node to have inserted node as left sibling
            }
            if ( node.right==null)
            {
                node.right = maxNode; // if maxNode.right node does not exist make maxNode the righ child as well
                maxNode.left = node; // thus creating a circular list between 2 top level nodes
            }
            if (node.key > maxNode.key) { //if update max pointer if newly inserted node has greater key.
                maxNode = node;
            }
        } else //when there is no maxNode new node becomes max node
        {
            maxNode = node;
        }
        numNodes++;
    }

 
    public FiboNode removeMax() // removes and returns the maxNode of the heap
    {
        FiboNode rMax = maxNode;
        if (rMax != null) {
            int numberofChildren = rMax.degree; 
            FiboNode child = rMax.child;
            FiboNode temp;

            while (numberofChildren > 0) { //if max has children
            	temp = child.right;

            	child.left.right = child.right; // remove child from child list
            	child.right.left = child.left;

            	child.left = maxNode; // add child to root list of heap
            	child.right = maxNode.right;
                maxNode.right = child;
                child.right.left = child;

                child.parent = null; // remove parent pointer
                child = temp;
                
                numberofChildren--; //decreases number of children of the maxNode
            }

            rMax.left.right = rMax.right; // remove max from root list of heap by updating left and right sibling pointers
            rMax.right.left = rMax.left;

            if (rMax == rMax.right) {
                maxNode = null;

            } else {
               maxNode = rMax.right;
               meld();
           }
           numNodes--;
           return rMax;
       }
        return null;
    }

 
    public void meld()  // melds 2 trees with same degree
    {
    	// Find the number of root nodes so as to be considered for meld operation
        int numRoots = 0;
        FiboNode mMax = maxNode;
        
        if (mMax != null) {
            numRoots++;
            mMax = mMax.right;                     

            while (mMax != maxNode) { //traverses the circular linked list in the topmost level
                numRoots++;
                mMax = mMax.right;
            }
        }
    	double phi = ((1.0 + Math.sqrt(5))/(2.0));
		int length = (int)Math.ceil(Math.log(numNodes)/Math.log(phi)); // Computes maximum possible degree for the Fibonacci heap
		
        List<FiboNode> degreeTable = new ArrayList<FiboNode>(length);

        for (int i = 0; i < length; i++) { // Initializing degree table
            degreeTable.add(null);
        }

        while (numRoots > 0) { //iterate for each node in the topmost level i.e each root node

            int d = mMax.degree;
            FiboNode next = mMax.right;

            // check in the degree table if node exists
            while(true) { 
                FiboNode node1 = degreeTable.get(d);
                if (node1 == null) break; // doesn't exist
                
                //determine whose key is greater
                if (mMax.key < node1.key) {
                    FiboNode temp = node1;
                    node1 = mMax;
                    mMax = temp;
                }

                meldAsChild(node1, mMax); //makes node with smaller key value the child of node with greater key value

                //update degreeTable
                degreeTable.set(d, null); // remove the node that is being melded
                d++;
            }

            degreeTable.set(d, mMax); //inserting into degree table

            // continue traversing
            mMax = next;
            numRoots--;
        }



        
        //Deleting the max node
        maxNode = null;

        // combine entries of the degree table
        for (int i = 0; i < length; i++) {
            FiboNode node2 = degreeTable.get(i);
            if (node2 == null) continue;

            //till max node is not null
            if (maxNode != null) {

                // First remove node from root list.
            	node2.left.right = node2.right;
            	node2.right.left = node2.left;

                // Now add to root list, again.
            	node2.left = maxNode;
            	node2.right = maxNode.right;
                maxNode.right = node2;
                node2.right.left = node2;

                // Check if this is a new maximum
                if (node2.key > maxNode.key) {
                    maxNode = node2;
                }
            } else {
                maxNode = node2;
            }
        }
    }
    
    
    public void delete(FiboNode node1, FiboNode node2) //performs delete operation by cutting node1 from node2
    {
    	// deleting node1 from child of node2
    	node1.right.left = node1.left;
    	node1.left.right = node1.right;
    	node2.degree--; // reduce degree of node2

        // updates child pointer for node2
        if (node2.child == node1) {
        	node2.child = node1.right;
        }

        if (node2.degree == 0) { // if there is no child of node2
        	node2.child = null;
        }

        // now we add the remove child to the topmost level as a root node and hence removing parent pointer
        node1.left = maxNode;
        node1.right = maxNode.right;
        maxNode.right = node1;
        node1.right.left = node1;
        node1.parent = null;

        // set childCut to false
        node1.childCut = false;
    }


    
    public void increaseKey(FiboNode node, int newVal) //Increases value of existing node in the Fibonacci Heap
    {
    	node.key = newVal;
        FiboNode parent = node.parent;

        if ((parent != null) && (node.key > parent.key)) { // checks if parent's key is lesser than child's key.
            delete(node, parent);		//deletes node from parent child and inserts into root
            checkCascadingCut(parent);
        }

        if (node.key > maxNode.key) { //replace max if new node.key is greater than max.key
            maxNode = node; 
        }
    }

//-----------------------------------------------Auxiliary Functions-------------------------------------------------------
 
    public void meldAsChild(FiboNode n1,FiboNode n2)  //meld n1 and node n2 making n2 the root
    {
    	// remove n1 from top most level
        n1.left.right = n1.right;
        n1.right.left = n1.left;

        // make n1 child of n2
        n1.parent = n2;
        if (n2.child == null) {
        	n2.child = n1;
            n1.right = n1;
            n1.left = n1;
        } else {
        	n1.left = n2.child;
        	n1.right = n2.child.right;
        	n2.child.right = n1;
            n1.right.left = n1;
        }
        n2.degree++; // increase number of children

        n1.childCut = false; //update childCut value
    }
    
  
    public void checkCascadingCut(FiboNode node) //Checks and performs cascading cut based on parent's childCut values
    {
        FiboNode parent = node.parent;

        if (parent != null) { //parent exists

            if (!node.childCut) {
            	node.childCut = true; // update childCut
            } else { //childCut == true
                
                delete(node, parent); // delete parent as well
                checkCascadingCut(parent); // check again recursively
            }
        }
    }

}