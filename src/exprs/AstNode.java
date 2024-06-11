package exprs;

import java.util.List;

abstract class AstNode {

    private final List<AstNode> CHILDREN;

    AstNode(AstNode... children) {
        this(List.of(children));
    }

    AstNode(List<AstNode> children) {
        this.CHILDREN = children;
    }

    void addChild(AstNode child) {
        this.CHILDREN.add(child);
    }

    List<AstNode> getChildren() {
        return this.CHILDREN;
    }

    int size() {
        return this.CHILDREN.size();
    }
}
