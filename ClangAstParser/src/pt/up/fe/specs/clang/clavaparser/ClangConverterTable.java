/**
 * Copyright 2016 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.clang.clavaparser;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.clang.ast.ClangNode;
import pt.up.fe.specs.clang.ast.genericnode.ClangRootNode.ClangRootData;
import pt.up.fe.specs.clang.clavaparser.extra.UndefinedParser;
import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ast.ClavaNodeFactory;
import pt.up.fe.specs.clava.ast.expr.Expr;
import pt.up.fe.specs.clava.ast.extra.Undefined;
import pt.up.fe.specs.clava.ast.type.Type;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.io.FileService;
import pt.up.fe.specs.util.io.LineStreamFileService;

public class ClangConverterTable implements AutoCloseable {

    private static final Set<String> PARSED_NODES = Collections.synchronizedSet(new LinkedHashSet<>());

    // This is currently not working
    private static final boolean CACHE_PARSED_NODES = false;
    private final Map<String, ClangNodeParser<?>> converter;
    // Maps types addresses to its types
    private Map<String, Type> originalTypes;
    // Maps AST nodes to the corresponding types
    private Map<String, Type> types;
    private ClangRootData clangRootData;
    private Map<String, ClavaNode> parsedNodes;
    private final FileService fileService;

    public ClangConverterTable(ClangRootData clangRootData) {
        this.converter = new HashMap<>();
        types = new HashMap<>();
        originalTypes = null;
        this.clangRootData = clangRootData;
        parsedNodes = new HashMap<>();
        fileService = new LineStreamFileService();
    }

    public void setTypesMapping(Map<String, Type> types) {
        this.types = types;
    }

    public Map<String, Type> getTypes() {
        if (types.isEmpty()) {
            throw new RuntimeException("TypesMap has not been initialized");
        }
        return types;
    }

    public FileService getFileService() {
        return fileService;
    }

    public Map<String, ClavaNode> getParsedNodes() {
        return parsedNodes;
    }

    public void setOriginalTypes(Map<String, Type> originalTypes) {
        this.originalTypes = originalTypes;
    }

    public Map<String, Type> getOriginalTypes() {
        if (originalTypes == null) {
            throw new RuntimeException("Original types has not been initialized");
        }
        return originalTypes;
    }

    public ClangRootData getClangRootData() {
        return clangRootData;
    }

    public void put(String nodeName, ClangNodeParser nodeParser) {
        if (converter.containsKey(nodeParser)) {
            SpecsLogs.msgWarn("Parser already set for nodes of type '" + nodeName + "', overriding it");
        }

        converter.put(nodeName, nodeParser);
    }

    /**
     * Helper method which accepts a ClangNodeParser provider that accepts a ClangConverterTable.
     * 
     * 
     * @param nodeName
     * @param nodeParser
     */
    public void put(String nodeName, Function<ClangConverterTable, ClangNodeParser> nodeParser) {
        put(nodeName, nodeParser.apply(this));
    }

    public ClavaNode parse(ClangNode clangNode) {

        // Add node name
        PARSED_NODES.add(clangNode.getNodeName());

        // If map does not have a conversor, stop
        if (!converter.containsKey(clangNode.getName())) {
            // Check if it has content
            boolean hasContent = !clangNode.getContentTry().orElse("").isEmpty();

            UndefinedParser undefinedParser = new UndefinedParser(this, hasContent);
            return undefinedParser.parse(clangNode);

            // List<ClavaNode> children = clangNode.getChildrenStream()
            // .map(child -> parse(child))
            // .collect(Collectors.toList());
            //
            // return ClavaNodeFactory.undefined(clangNode.getName(), clangNode.getDescription(), clangNode.getInfo(),
            // children);

        }

        // Before converting, check if the node has already been converted
        Optional<String> id = clangNode.getExtendedIdTry();
        if (CACHE_PARSED_NODES) {
            if (id.isPresent()) {
                ClavaNode parsedClavaNode = parsedNodes.get(id.get());
                if (parsedClavaNode != null) {
                    return parsedClavaNode.copy();
                }
            }

        }

        try {

            ClavaNode clavaNode = converter.get(clangNode.getName()).parse(clangNode);

            /*
            // Store node in map if it has an id
            if (id.isPresent()) {
            parsedNodes.put(id.get(), clavaNode);
            }
              */
            // Store node in map if it has an id
            if (CACHE_PARSED_NODES) {
                if (id.isPresent()) {
                    ClavaNode previousClavaNode = parsedNodes.put(id.get(), clavaNode);
                    Preconditions.checkArgument(previousClavaNode == null, "Expected node to not be in the table");
                }
            }

            return clavaNode;
        } catch (Throwable t) {
            String message = "Error while parsing node '" + clangNode.getName() + "' at " + clangNode.getLocation()
                    + "\nContents: " + clangNode.getContentTry().orElse("<no content>");
            throw new RuntimeException(message, t);
        }
    }

    public List<ClavaNode> parse(List<ClangNode> nodes) {
        return nodes.stream()
                .map(child -> parse(child))
                .collect(Collectors.toList());
    }

    public Expr parseAsExpr(ClangNode node) {
        ClavaNode exprNode = parse(node);

        // If Undefined, convert to DummyExpr
        if (exprNode instanceof Undefined) {
            // exprNode = ClavaNodeFactory.dummyExpr((node.getDescription() + " -> " + exprNode.toContentString()),
            exprNode = ClavaNodeFactory.dummyExpr(exprNode.toContentString(),
                    exprNode.getInfo(),
                    exprNode.getChildren());
        }

        Preconditions.checkArgument(exprNode instanceof Expr, "Expected an expr");

        return (Expr) exprNode;
    }

    /**
     * 
     * @return a Set with the names of the nodes seen so far that have no parser yet
     */
    public static Set<String> getUndefinedNodes() {
        return PARSED_NODES;
    }

    public void setClangRootData(ClangRootData clangRootData) {
        this.clangRootData = clangRootData;
    }

    @Override
    public void close() throws Exception {
        fileService.close();
    }

}
