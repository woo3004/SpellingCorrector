package spell;

public class Trie implements ITrie{
    private INode root = new Node();
    private int wordCount = 0;
    private int nodeCount = 1;
    @Override
    public void add(String word) {
        INode currNode = root;
        for(int i = 0; i < word.length(); i++) {
            if(currNode.getChildren()[(int)word.toLowerCase().charAt(i)-'a'] == null) {
                currNode.getChildren()[(int)word.toLowerCase().charAt(i)-'a'] = new Node();
                nodeCount++;
            }
            currNode = currNode.getChildren()[(int)word.toLowerCase().charAt(i)-'a'];
            if(i == word.length() - 1) {
                if(currNode.getValue() == 0) {
                    wordCount++;
                }
                currNode.incrementValue();
            }
        }
    }
    @Override
    public INode find(String word) {
        INode currNode = root;
        for(int i = 0; i < word.length(); i++) {
            if(currNode.getChildren()[(int)word.toLowerCase().charAt(i)-'a'] == null) {
                return null;
            }
            currNode = currNode.getChildren()[(int)word.toLowerCase().charAt(i)-'a'];
            if(i == word.length() - 1) {
                if(currNode.getValue() == 0) {
                    return null;
                }
            }
        }
        return currNode;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }
    @Override
    public int getNodeCount() {
        return nodeCount;
    }
    @Override
    public int hashCode() {
        int myHash = wordCount * nodeCount;
        for(int i = 0; i < root.getChildren().length; i++) {
            if(root.getChildren()[i] != null) {
                myHash = myHash * i;
            }
        }
        return myHash;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(obj.getClass() != this.getClass()) {
            return false;
        }

        Trie trie = (Trie)obj;

        if(trie.getWordCount() != this.getWordCount()) {
            return false;
        }
        if(trie.getNodeCount() != this.getNodeCount()) {
            return false;
        }

        return equals_Helper(this.root, trie.root);
    }
    private boolean equals_Helper(INode node1, INode node2) {
        if(node1.getValue() != node2.getValue()) {
            return false;
        }
        for (int i = 0; i < node1.getChildren().length; i++) {
            if (node1.getChildren()[i] != null || node2.getChildren()[i] != null) {
                if (node1.getChildren()[i] != null && node2.getChildren()[i] != null) {
                    if (!equals_Helper(node1.getChildren()[i], node2.getChildren()[i])) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public String toString() {
        StringBuilder currWord = new StringBuilder();
        StringBuilder output = new StringBuilder();

        toString_Helper(root, currWord, output);

        return output.toString();
    }
    private void toString_Helper(INode node, StringBuilder currWord, StringBuilder output) {
        if(node.getValue() >0) {
            output.append(currWord.toString());
            output.append("\n");
        }
        INode [] children = node.getChildren();
        for(int i = 0; i < children.length; i++) {
            if(children[i] != null) {
                char c = (char)('a' +i);
                currWord.append(c);
                toString_Helper(children[i], currWord, output);
                currWord.deleteCharAt(currWord.length() - 1);
            }
        }
    }
}
