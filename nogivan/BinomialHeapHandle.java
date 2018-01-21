package nogivan;


public class BinomialHeapHandle<T extends Comparable<T>> {
  private BinomialTreeNode<T> node;
  
  BinomialTreeNode<T> getNode() {
    return node;
  }
  
  void setNode(BinomialTreeNode<T> node) {
    this.node = node;
  }
  
  BinomialHeapHandle(BinomialTreeNode<T> node) {
    this.node = node;
  }
}
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
