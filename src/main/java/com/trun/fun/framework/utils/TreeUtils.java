package com.trun.fun.framework.utils;


import com.trun.fun.framework.model.TreeNode;

import java.util.List;
import java.util.Objects;


/**
 * <p>
 * Tree工具类
 * </p>
 *
 * @author Caratacus
 */
public abstract class TreeUtils {

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public static <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
        treeNodes.stream().filter(e -> Objects.equals(treeNode.getId(), e.getParentId()))
                .forEach(e -> treeNode.getChildrens().add(findChildren(e, treeNodes)));
        return treeNode;
    }

    public static <T extends TreeNode> T findChildrenUUID(T treeNode, List<T> treeNodes) {
        treeNodes.stream().filter(e -> (treeNode.getUUID().equalsIgnoreCase(e.getParentUUID())))
                .forEach(e -> treeNode.getChildrens().add(findChildrenUUID(e, treeNodes)));
        return treeNode;
    }
}
