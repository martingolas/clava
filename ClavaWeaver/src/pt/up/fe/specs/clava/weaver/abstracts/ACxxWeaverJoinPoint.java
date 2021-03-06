package pt.up.fe.specs.clava.weaver.abstracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.lara.interpreter.utils.DefMap;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.clava.ClavaLog;
import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaNodeParser;
import pt.up.fe.specs.clava.SourceLocation;
import pt.up.fe.specs.clava.ast.expr.ImplicitCastExpr;
import pt.up.fe.specs.clava.ast.type.Type;
import pt.up.fe.specs.clava.utils.Typable;
import pt.up.fe.specs.clava.weaver.CxxActions;
import pt.up.fe.specs.clava.weaver.CxxAttributes;
import pt.up.fe.specs.clava.weaver.CxxJoinpoints;
import pt.up.fe.specs.clava.weaver.CxxWeaver;
import pt.up.fe.specs.clava.weaver.Insert;
import pt.up.fe.specs.clava.weaver.abstracts.joinpoints.AJoinPoint;
import pt.up.fe.specs.clava.weaver.abstracts.joinpoints.AStatement;
import pt.up.fe.specs.clava.weaver.importable.LowLevelApi;
import pt.up.fe.specs.clava.weaver.joinpoints.CxxProgram;
import pt.up.fe.specs.clava.weaver.joinpoints.types.CxxType;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Abstract class which can be edited by the developer. This class will not be overwritten.
 *
 * @author Lara Weaver Generator
 */
public abstract class ACxxWeaverJoinPoint extends AJoinPoint {

    private static final Set<Class<? extends ClavaNode>> IGNORE_NODES;
    static {
        IGNORE_NODES = new HashSet<>();
        IGNORE_NODES.add(ImplicitCastExpr.class);
        // IGNORE_NODES.add(ParenExpr.class); // Have not tried it yet
    }

    @Override
    public CxxWeaver getWeaverEngine() {
        return super.getWeaverEngine();
    }

    /**
     * Implementation of GET_NAME. Returns null if no mapping is found.
     */
    /*
    private static final String GET_NAME_DEFAULT = "!NO_NAME!";
    private static final FunctionClassMap<ClavaNode, String> GET_NAME;
    static {
        GET_NAME = new FunctionClassMap<>(GET_NAME_DEFAULT);
        GET_NAME.put(NamedDecl.class, namedDecl -> namedDecl.hasDeclName() ? namedDecl.getDeclName() : null);
        GET_NAME.put(TagType.class, tagType -> tagType.getDeclInfo().getDeclName());
        GET_NAME.put(DeclRefExpr.class, declRef -> declRef.getRefName());
        GET_NAME.put(Stmt.class, stmt -> stmt.getClass().getSimpleName());
    }
    */

    /**
     * Compares the two join points based on their node reference of the used compiler/parsing tool.<br>
     * This is the default implementation for comparing two join points. <br>
     * <b>Note for developers:</b> A weaver may override this implementation in the editable abstract join point, so the
     * changes are made for all join points, or override this method in specific join points.
     */
    @Override
    public boolean compareNodes(AJoinPoint aJoinPoint) {
        return getNode().equals(aJoinPoint.getNode());
    }

    @Override
    public CxxProgram getRootImpl() {
        return getWeaverEngine().getAppJp();
        /*
        ACxxWeaverJoinPoint current = this;
        while (current.getHasParentImpl()) {
            current = current.getParentImpl();
        }
        
        
        return (current instanceof CxxProgram) ? (CxxProgram) current : null;
        */
        // Preconditions.checkArgument(current instanceof CxxProgram,
        // "Expected root joinpoint to be a CxxProgram, it is a '" + current.getClass().getSimpleName() + "'");
        //
        // return (CxxProgram) current;
    }

    /**
     *
     * @return the parent joinpoint
     */
    @Override
    public abstract ACxxWeaverJoinPoint getParentImpl();

    @Override
    public ACxxWeaverJoinPoint getAstParentImpl() {
        ClavaNode node = getNode();
        if (!node.hasParent()) {
            return null;
        }

        ClavaNode currentParent = node.getParent();
        // if (currentParent instanceof WrapperStmt) {
        // currentParent = currentParent.getParent();
        // }

        return CxxJoinpoints.create(currentParent, this);
    }

    @Override
    public Boolean getHasAstParentImpl() {
        return getNode().hasParent();
    }

    @Override
    public AJoinPoint ancestorImpl(String type) {
        Preconditions.checkNotNull(type, "Missing type of ancestor in attribute 'ancestor'");

        if (type.equals("program")) {
            ClavaLog.warning("Consider using attribute .root, instead of .ancestor('program')");
        }

        ClavaNode currentNode = getNode();
        while (currentNode.hasParent()) {
            ACxxWeaverJoinPoint parentJp = CxxJoinpoints.create(currentNode.getParent(), this);
            if (parentJp.getJoinpointType().equals(type)) {
                return parentJp;
            }

            currentNode = parentJp.getNode();
        }

        return null;
    }

    @Override
    public AJoinPoint[] descendantsArrayImpl(String type) {
        Preconditions.checkNotNull(type, "Missing type of descendants in attribute 'ancestor'");

        return getNode().getDescendantsStream()
                .map(descendant -> CxxJoinpoints.create(descendant, this))
                .filter(jp -> jp.getJoinpointType().equals(type))
                .toArray(AJoinPoint[]::new);

    }

    @Override
    public AJoinPoint[] descendantsAndSelfArrayImpl(String type) {
        Preconditions.checkNotNull(type, "Missing type of descendants in attribute 'ancestor'");

        return getNode().getDescendantsAndSelfStream()
                .map(descendant -> CxxJoinpoints.create(descendant, this))
                .filter(jp -> jp.getJoinpointType().equals(type))
                .toArray(AJoinPoint[]::new);
    }

    @Override
    public AJoinPoint chainAncestorImpl(String type) {
        Preconditions.checkNotNull(type, "Missing type of ancestor in attribute 'chainAncestor'");

        if (type.equals("program")) {
            ClavaLog.warning("Consider using attribute .root, instead of .chainAncestor('program')");
        }

        ACxxWeaverJoinPoint currentJp = this;
        while (currentJp.getHasParentImpl()) {
            ACxxWeaverJoinPoint parentJp = currentJp.getParentImpl();
            // if (parentJp.getJoinpointType().equals(type)) {
            if (parentJp.instanceOf(type)) {
                return parentJp;
            }

            currentJp = parentJp;
        }

        return null;
    }

    @Override
    public AJoinPoint astAncestorImpl(String type) {
        Preconditions.checkNotNull(type, "Missing type of ancestor in attribute 'astAncestor'");

        ClavaNode currentNode = getNode();
        while (currentNode.hasParent()) {
            ClavaNode parentNode = currentNode.getParent();

            if (parentNode.getNodeName().equals(type)) {
                return CxxJoinpoints.create(parentNode, this);
            }

            currentNode = parentNode;
        }

        /*
        ACxxWeaverJoinPoint currentJp = this;
        while (currentJp.getAstParent() != null) {
            ACxxWeaverJoinPoint parentJp = currentJp.getAstParent();
        
            if (parentJp.getJoinpointType().equals(type)) {
                return parentJp;
            }
        
            currentJp = parentJp;
        }
        */

        return null;
    }

    @Override
    public Boolean getHasParentImpl() {
        return getParent() != null;
    }

    @Override
    public String getAstImpl() {
        return getNode().toString();
    }

    @Override
    public String getCodeImpl() {
        return getNode().getCode();
    }

    @Override
    public Integer getLineImpl() {
        // ClavaNode node = getNode();
        // Preconditions.checkNotNull(node);
        int line = getNode().getLocation().getStartLine();
        return line != SourceLocation.getInvalidLoc() ? line : null;
    }

    @Override
    public String getJoinpointTypeImpl() {
        // return getNode().getClass().getSimpleName();
        return getJoinPointType();
        // String joinpointName = getClass().getSimpleName();
        //
        // // Remove 'CXX' prefix
        // if (joinpointName.startsWith("Cxx")) {
        // joinpointName = joinpointName.substring("Cxx".length());
        // }
        //
        // // Make first character lowercase
        // char lowerFirstChar = Character.toLowerCase(joinpointName.charAt(0));
        //
        // return lowerFirstChar + joinpointName.substring(1);
    }

    @Override
    public void insertImpl(String position, String code) {
        Insert insert = Insert.getHelper().valueOf(position);
        CxxActions.insertAsStmt(getNode(), code, insert, getWeaverEngine());
        //
        // if (insert == Insert.AFTER || insert == Insert.BEFORE) {
        // Stmt literalStmt = ClavaNodeFactory.literalStmt(code);
        // CxxActions.insertStmtAndBelow(getNode(), literalStmt, insert);
        // return;
        // }

    }

    @Override
    public void setTypeImpl(AJoinPoint type) {
        // Check if joinpoint is a type
        if (!(type instanceof CxxType)) {
            SpecsLogs.msgInfo(
                    "[setType] Passed a joinpoint that is not a Type ('" + type.getJoinpointType() + "'), ignoring");
            return;
        }

        CxxType cxxType = (CxxType) type;

        // Check if node has a type
        ClavaNode node = getNode();

        if (!(node instanceof Typable)) {
            SpecsLogs.msgLib("[Ignore] Setting type ('" + type.getNode().getNodeName()
                    + "') of a node that has no type ('" + node.getNodeName() + "')");
            return;
        }

        ((Typable) node).setType(cxxType.getNode());

    }

    @Override
    public AJoinPoint insertBeforeImpl(AJoinPoint node) {
        // Check if type
        if (node.getNode() instanceof Type) {
            ClavaLog.info("Action 'insertBefore' not available for 'type' join points");
            return null;
        }

        return CxxActions.insertBefore(this, node);
    }

    @Override
    public AJoinPoint insertBeforeImpl(String code) {
        // return insertBeforeImpl(CxxJoinpoints.create(ClavaNodeFactory.literalStmt(code), this));
        return insertBeforeImpl(CxxJoinpoints.create(ClavaNodeParser.parseStmt(code), this));
    }

    @Override
    public AJoinPoint insertAfterImpl(AJoinPoint node) {
        // Check if type
        if (node.getNode() instanceof Type) {
            ClavaLog.info("Action 'insertAfter' not available for 'type' join points");
            return null;
        }

        return CxxActions.insertAfter(this, node);
    }

    @Override
    public AJoinPoint insertAfterImpl(String code) {
        // return insertAfterImpl(CxxJoinpoints.create(ClavaNodeFactory.literalStmt(code), this));
        return insertAfterImpl(CxxJoinpoints.create(ClavaNodeParser.parseStmt(code), this));
    }

    @Override
    public AJoinPoint replaceWithImpl(AJoinPoint node) {
        CxxActions.replace(getNode(), node.getNode(), getWeaverEngine());

        // Return input joinpoint
        return node;
    }

    @Override
    public void detachImpl() {
        ClavaNode node = getNode();

        if (!node.hasParent()) {
            SpecsLogs.msgInfo(
                    "action detach: could not find a parent in joinpoint of type '" + getJoinpointType() + "'");
            return;
        }

        // If node is wrapped, detach wrapper
        ClavaNode parentNode = node.getParent();
        if (parentNode.isWrapper()) {
            parentNode.detach();
            return;
        }

        node.detach();
    }

    @Override
    public AJoinPoint getTypeImpl() {
        ClavaNode node = getNode();

        if (!(node instanceof Typable)) {
            SpecsLogs.msgInfo("Joinpoint of type '" + getJoinpointType() + "' with node '" + node.getNodeName()
                    + "' does not have a type");
            return null;
        }

        // return new CxxType(((Typable) node).getType(), this);
        return CxxJoinpoints.create(((Typable) node).getType(), this);
    }

    @Override
    public String toString() {
        return "Joinpoint '" + getJoinpointType() + "'";
    }

    /**
     * In case a joinpoint child needs to access the list of the parent joinpoint statements.
     *
     * @return
     */
    public List<? extends AStatement> selectStatements() {
        throw new RuntimeException("Not supported for joinpoint '" + getClass() + "'");
    }
    /*
    @Override
    public String getName() {
    
        String name = GET_NAME.apply(getNodeNormalized());
    
        if (name != null && name.equals(GET_NAME_DEFAULT)) {
            CxxLog.warning("attribute 'name' not implemented for joinpoint '" + getClass().getSimpleName() + "'");
            return null;
        }
    
        return name;
        /*
        // TODO: Add .getName() to ClavaNode, returning an Optional
    
        // ClavaNode node = getNode();
        ClavaNode node = getNodeNormalized();
        if (node instanceof NamedDecl) {
            NamedDecl namedDecl = ((NamedDecl) node);
            return namedDecl.hasDeclName() ? namedDecl.getDeclName() : null;
        }
    
        // if (node instanceof Type) {
        // return node.getNodeName();
        // }
    
        if (node instanceof TagType) {
            return ((TagType) node).getDeclInfo().getDeclName();
        }
    
        if (node instanceof DeclRefExpr) {
            return ((DeclRefExpr) node).getRefName();
        }
    
        if (node instanceof Stmt) {
            return ((Stmt) node).getClass().getSimpleName();
        }
    
        CxxLog.warning("attribute 'name' not implemented for joinpoint '" + getClass().getSimpleName() + "'");
        return null;
        // throw new RuntimeException(
        // "attribute 'name' not implemented for joinpoint '" + getClass().getSimpleName() + "'");
        // return "<undefined_name>";
         *
         */
    // }

    @Override
    public String getLocationImpl() {
        return getNode().getLocation().toString();
    }

    @Override
    public Boolean containsImpl(AJoinPoint jp) {
        ClavaNode clavaNode = jp.getNode();

        return getNode().getDescendantsStream()
                .filter(child -> child == clavaNode)
                .findFirst().isPresent();
    }

    @Override
    public void defImpl(String attribute, Object value) {
        // Get def map
        DefMap<?> defMap = getDefMap();

        if (defMap == null) {
            SpecsLogs
                    .msgInfo("Joinpoint '" + getJoinpointType() + "' does not have 'def' defined for any attribute");
            return;
        }

        if (!defMap.hasAttribute(attribute)) {
            List<String> keys = new ArrayList<>(defMap.keys());
            Collections.sort(keys);
            SpecsLogs
                    .msgInfo("'def' of attribute '" + attribute + "' not defined for joinpoint " + getJoinpointType());
            SpecsLogs.msgInfo("Available attributes: " + keys);
            return;
        }

        defMap.apply(attribute, this, value);
    }

    protected DefMap<?> getDefMap() {
        return null;
    }

    /**
     * Ignores certain nodes, such as ImplicitCastExpr.
     *
     * @return
     */
    public ClavaNode getNodeNormalized() {
        ClavaNode currentNode = getNode();

        while (IGNORE_NODES.contains(currentNode.getClass())) {
            Preconditions.checkArgument(currentNode.getNumChildren() == 1,
                    "Expected node to have one child:\n" + currentNode);
            currentNode = currentNode.getChild(0);
        }

        return currentNode;
    }

    @Override
    public Integer getAstNumChildrenImpl() {
        ClavaNode node = getNode();
        if (node == null) {
            return -1;
        }

        return node.getNumChildren();
    }

    @Override
    public AJoinPoint astChildImpl(Integer index) {
        ClavaNode node = getNode();
        if (node == null) {
            return null;
        }

        if (index >= node.getNumChildren()) {
            ClavaLog.warning(
                    "Index '" + index + "' is out of range, node only has " + node.getNumChildren() + " children");
            return null;
        }

        return CxxJoinpoints.create(node.getChild(index), this);
    }

    @Override
    public String[] getChainArrayImpl() {
        List<String> chain = new ArrayList<>();

        AJoinPoint currentJoinpoint = this;
        while (currentJoinpoint != null) {
            // Add joinpoint to chain
            chain.add(currentJoinpoint.getJoinpointTypeImpl());

            // Update current joinpoint
            if (currentJoinpoint.getHasParentImpl()) {
                currentJoinpoint = currentJoinpoint.getParentImpl();
            } else {
                currentJoinpoint = null;
            }
        }

        // Inverse order of the list
        Collections.reverse(chain);

        return chain.toArray(new String[0]);
    }

    @Override
    public String getAstNameImpl() {
        return getNodeNormalized().getNodeName();
    }

    @Override
    public String[] getJavaFieldsArrayImpl() {
        return LowLevelApi.getFields(getNode()).toArray(new String[0]);
    }

    @Override
    public String javaFieldTypeImpl(String fieldName) {
        return LowLevelApi.getFieldClass(getNode(), fieldName).getName();
    }

    @Override
    public Object javaValueImpl(String fieldName) {
        return LowLevelApi.getValue(getNode(), fieldName);
    }

    @Override
    public String getAstIdImpl() {
        return getNode().getExtendedId().orElse("<NO_ID>");
    }

    @Override
    public Boolean getIsInsideLoopHeaderImpl() {
        return CxxAttributes.isInsideLoopHeader(getNode());
    }

    @Override
    public Object getUserFieldImpl(String fieldName) {
        ClavaLog.deprecated("attribute 'getUserField' is deprecated, please use 'userField' instead");
        return userFieldImpl(fieldName);
        // return getWeaverEngine().getUserField(getNodeNormalized(), fieldName);
    }

    @Override
    public Object userFieldImpl(String fieldName) {
        return getWeaverEngine().getUserField(getNodeNormalized(), fieldName);
    }

    @Override
    public Object setUserFieldImpl(String fieldName, Object value) {
        return getWeaverEngine().setUserField(getNodeNormalized(), fieldName, value);
    }

    @Override
    public Object setUserFieldImpl(Map<?, ?> fieldNameAndValue) {

        Object lastPrevious = null;
        for (Entry<?, ?> entry : fieldNameAndValue.entrySet()) {
            lastPrevious = setUserField(entry.getKey().toString(), entry.getValue());
        }

        return lastPrevious;
    }
    // @Override
    // public Object setUserFieldImpl(Object fieldNameAndValue) {
    // System.out.println("CLASS:" + fieldNameAndValue.getClass());
    // System.out.println("VALUE:" + fieldNameAndValue);
    // return super.setUserFieldImpl(fieldNameAndValue);
    // }

    @Override
    public AJoinPoint getParentRegionImpl() {

        return CxxAttributes.getParentRegion(getNode())
                .map(node -> CxxJoinpoints.create(node, this))
                .orElse(null);
        /*
        Optional<? extends ClavaNode> parentRegionTry = CxxAttributes.getParentRegion(getNode());
        
        if (!parentRegionTry.isPresent()) {
            ClavaLog.info("Join point '" + getJoinPointType() + "' does not support parentRegion");
            return null;
        }
        
        return CxxJoinpoints.create(parentRegionTry.get(), this);
        */
        /*
        // Get current region
        ClavaNode currentRegion = getCurrentRegion(getNode());
        if (currentRegion == null) {
            ClavaLog.info("Join point '" + getJoinPointType() + "' does not support parentRegion");
            return null;
        }
        
        // If already at top region, return that node
        if (currentRegion instanceof TranslationUnit) {
            return CxxJoinpoints.create(currentRegion, this);
        }
        System.out.println("CURRENT REGION:" + currentRegion.getNodeName() + ", " + currentRegion.getLocation());
        System.out.println(
                "PARENT:" + currentRegion.getParent().getNodeName() + ", " + currentRegion.getParent().getLocation());
        System.out.println("PARENT REGION" + getCurrentRegion(currentRegion.getParent()).getNodeName() + ", "
                + getCurrentRegion(currentRegion.getParent()).getLocation());
        // Go up one node, and return the current region
        return CxxJoinpoints.create(getCurrentRegion(currentRegion.getParent()), this);
        */
    }

    @Override
    public AJoinPoint getCurrentRegionImpl() {
        Optional<? extends ClavaNode> currentRegionTry = CxxAttributes.getCurrentRegion(getNode());

        if (!currentRegionTry.isPresent()) {
            ClavaLog.info("Join point '" + getJoinPointType() + "' does not support currentRegion");
            return null;
        }

        return CxxJoinpoints.create(currentRegionTry.get(), this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AJoinPoint)) {
            return false;
        }

        return getNode().equals(((AJoinPoint) obj).getNode());
    }

    @Override
    public int hashCode() {
        return getNode().hashCode();
    }

    @Override
    public AJoinPoint copyImpl() {
        return CxxJoinpoints.create(getNode().copy(), null);
    }

    @Override
    public AJoinPoint[] getAstChildrenArrayImpl() {
        return getNode().getChildren().stream()
                .map(node -> CxxJoinpoints.create(node, this))
                .collect(Collectors.toList())
                .toArray(new AJoinPoint[0]);

    }

    @Override
    public Boolean hasNodeImpl(Object nodeOrJp) {
        if (nodeOrJp instanceof AJoinPoint) {
            return hasNodeImpl(((AJoinPoint) nodeOrJp).getNode());
        }

        if (nodeOrJp instanceof ClavaNode) {
            return getNode() == nodeOrJp;
        }

        ClavaLog.warning("joinpoint attribute 'hasNode': input type '" + nodeOrJp.getClass()
                + "' not supported, returning false");
        return false;
    }

    // @Override
    // public List<? extends ACxxWeaverJoinPoint> selectDescendant() {
    // return getNode().getDescendantsStream()
    // .map(descendant -> CxxJoinpoints.create(descendant, this))
    // .collect(Collectors.toList());
    // }

}
