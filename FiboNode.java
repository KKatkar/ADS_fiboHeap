public class FiboNode
{
        FiboNode left, right, child, parent;
        int degree;       // number of children
        boolean childCut; // determines if child is cut
        String hashtag; 
        int key;

        FiboNode(String hashtag,int key)
        {
           this.hashtag = hashtag;
           this.key = key;

        }

    }