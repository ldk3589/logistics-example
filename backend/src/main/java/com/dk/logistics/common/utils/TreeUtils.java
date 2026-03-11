package com.dk.logistics.common.utils;
import com.dk.logistics.module.system.menu.vo.MenuTreeVO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeUtils {

    public static List<MenuTreeVO> buildMenuTree(List<MenuTreeVO> allNodes) {
        if (allNodes == null || allNodes.isEmpty()) {
            return new ArrayList<>();
        }

        List<MenuTreeVO> roots = allNodes.stream()
                .filter(node -> node.getParentId() != null && node.getParentId() == 0L)
                .collect(Collectors.toList());

        for (MenuTreeVO root : roots) {
            fillChildren(root, allNodes);
        }

        return roots;
    }

    private static void fillChildren(MenuTreeVO parent, List<MenuTreeVO> allNodes) {
        List<MenuTreeVO> children = allNodes.stream()
                .filter(node -> node.getParentId() != null && node.getParentId().equals(parent.getId()))
                .collect(Collectors.toList());

        parent.setChildren(children);

        for (MenuTreeVO child : children) {
            fillChildren(child, allNodes);
        }
    }
}