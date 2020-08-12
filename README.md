# ADS_fiboHeap
max Fibonacci Heap implementation in Java
Implemented a system to find the "n" most popular hashtags that appear on social media such as Facebook or Twitter. 
For the scope of this project hashtags will be given from an input file.
Basic idea for the implementation is to use a max priority structure to find out the most popular hashtags.

<h3>Data Structures  used for implementation</h3>
- Max Fibonacci heap: use to keep track of the frequencies of hashtags.
- Hash table: The key for the hashtable is the hashtag,and the value is the pointer to the corresponding node in the Fibonacci heap.

**Assumption**
There will be a large number of hashtags appearing in the stream and the increase key operation will be performed many times.

__Max Fibonacciheap is recommended because it has an amortized complexity of O(1) fortheincrease key operation.__
