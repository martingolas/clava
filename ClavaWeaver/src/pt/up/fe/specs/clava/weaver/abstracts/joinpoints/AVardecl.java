package pt.up.fe.specs.clava.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.AttributeException;
import java.util.List;
import org.lara.interpreter.exception.ActionException;
import java.util.Map;
import org.lara.interpreter.weaver.interf.JoinPoint;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * Auto-Generated class for join point AVardecl
 * This class is overwritten by the Weaver Generator.
 * 
 * 
 * @author Lara Weaver Generator
 */
public abstract class AVardecl extends ANamedDecl {

    protected ANamedDecl aNamedDecl;

    /**
     * 
     */
    public AVardecl(ANamedDecl aNamedDecl){
        super(aNamedDecl);
        this.aNamedDecl = aNamedDecl;
    }
    /**
     * true, if vardecl has an initialization value
     */
    public abstract Boolean getHasInitImpl();

    /**
     * true, if vardecl has an initialization value
     */
    public final Object getHasInit() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "hasInit", Optional.empty());
        	}
        	Boolean result = this.getHasInitImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "hasInit", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "hasInit", e);
        }
    }

    /**
     * If vardecl has an initialization value, returns an expression with that value
     */
    public abstract AJoinPoint getInitImpl();

    /**
     * If vardecl has an initialization value, returns an expression with that value
     */
    public final Object getInit() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "init", Optional.empty());
        	}
        	AJoinPoint result = this.getInitImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "init", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "init", e);
        }
    }

    /**
     * true, if vardecl is a function parameter
     */
    public abstract Boolean getIsParamImpl();

    /**
     * true, if vardecl is a function parameter
     */
    public final Object getIsParam() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "isParam", Optional.empty());
        	}
        	Boolean result = this.getIsParamImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "isParam", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "isParam", e);
        }
    }

    /**
     * Get value on attribute storageClass
     * @return the attribute's value
     */
    public abstract String getStorageClassImpl();

    /**
     * Get value on attribute storageClass
     * @return the attribute's value
     */
    public final Object getStorageClass() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "storageClass", Optional.empty());
        	}
        	String result = this.getStorageClassImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "storageClass", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "storageClass", e);
        }
    }

    /**
     * Method used by the lara interpreter to select inits
     * @return 
     */
    public abstract List<? extends AExpression> selectInit();

    /**
     * 
     * @param init 
     */
    public void setInitImpl(AJoinPoint init) {
        throw new UnsupportedOperationException(get_class()+": Action setInit not implemented ");
    }

    /**
     * 
     * @param init 
     */
    public final void setInit(AJoinPoint init) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "setInit", this, Optional.empty(), init);
        	}
        	this.setInitImpl(init);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "setInit", this, Optional.empty(), init);
        	}
        } catch(Exception e) {
        	throw new ActionException(get_class(), "setInit", e);
        }
    }

    /**
     * 
     * @param init 
     */
    public void setInitImpl(String init) {
        throw new UnsupportedOperationException(get_class()+": Action setInit not implemented ");
    }

    /**
     * 
     * @param init 
     */
    public final void setInit(String init) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "setInit", this, Optional.empty(), init);
        	}
        	this.setInitImpl(init);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "setInit", this, Optional.empty(), init);
        	}
        } catch(Exception e) {
        	throw new ActionException(get_class(), "setInit", e);
        }
    }

    /**
     * Get value on attribute name
     * @return the attribute's value
     */
    @Override
    public String getNameImpl() {
        return this.aNamedDecl.getNameImpl();
    }

    /**
     * Get value on attribute isPublic
     * @return the attribute's value
     */
    @Override
    public Boolean getIsPublicImpl() {
        return this.aNamedDecl.getIsPublicImpl();
    }

    /**
     * 
     */
    public void defNameImpl(String value) {
        this.aNamedDecl.defNameImpl(value);
    }

    /**
     * 
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithImpl(AJoinPoint node) {
        return this.aNamedDecl.replaceWithImpl(node);
    }

    /**
     * 
     * @param node 
     */
    @Override
    public AJoinPoint insertBeforeImpl(AJoinPoint node) {
        return this.aNamedDecl.insertBeforeImpl(node);
    }

    /**
     * 
     * @param node 
     */
    @Override
    public AJoinPoint insertBeforeImpl(String node) {
        return this.aNamedDecl.insertBeforeImpl(node);
    }

    /**
     * 
     * @param node 
     */
    @Override
    public AJoinPoint insertAfterImpl(AJoinPoint node) {
        return this.aNamedDecl.insertAfterImpl(node);
    }

    /**
     * 
     * @param code 
     */
    @Override
    public AJoinPoint insertAfterImpl(String code) {
        return this.aNamedDecl.insertAfterImpl(code);
    }

    /**
     * 
     */
    @Override
    public void detachImpl() {
        this.aNamedDecl.detachImpl();
    }

    /**
     * 
     * @param type 
     */
    @Override
    public void setTypeImpl(AJoinPoint type) {
        this.aNamedDecl.setTypeImpl(type);
    }

    /**
     * 
     */
    @Override
    public AJoinPoint copyImpl() {
        return this.aNamedDecl.copyImpl();
    }

    /**
     * 
     * @param fieldName 
     * @param value 
     */
    @Override
    public Object setUserFieldImpl(String fieldName, Object value) {
        return this.aNamedDecl.setUserFieldImpl(fieldName, value);
    }

    /**
     * 
     * @param fieldNameAndValue 
     */
    @Override
    public Object setUserFieldImpl(Map<?, ?> fieldNameAndValue) {
        return this.aNamedDecl.setUserFieldImpl(fieldNameAndValue);
    }

    /**
     * 
     * @param message 
     */
    @Override
    public void messageToUserImpl(String message) {
        this.aNamedDecl.messageToUserImpl(message);
    }

    /**
     * 
     * @param position 
     * @param code 
     */
    @Override
    public void insertImpl(String position, String code) {
        this.aNamedDecl.insertImpl(position, code);
    }

    /**
     * 
     */
    @Override
    public String toString() {
        return this.aNamedDecl.toString();
    }

    /**
     * 
     */
    @Override
    public Optional<? extends ANamedDecl> getSuper() {
        return Optional.of(this.aNamedDecl);
    }

    /**
     * 
     */
    @Override
    public List<? extends JoinPoint> select(String selectName) {
        List<? extends JoinPoint> joinPointList;
        switch(selectName) {
        	case "init": 
        		joinPointList = selectInit();
        		break;
        	default:
        		joinPointList = this.aNamedDecl.select(selectName);
        		break;
        }
        return joinPointList;
    }

    /**
     * 
     */
    @Override
    public void defImpl(String attribute, Object value) {
        switch(attribute){
        case "name": {
        	if(value instanceof String){
        		this.defNameImpl((String)value);
        		return;
        	}
        	this.unsupportedTypeForDef(attribute, value);
        }
        default: throw new UnsupportedOperationException("Join point "+get_class()+": attribute '"+attribute+"' cannot be defined");
        }
    }

    /**
     * 
     */
    @Override
    protected void fillWithAttributes(List<String> attributes) {
        this.aNamedDecl.fillWithAttributes(attributes);
        attributes.add("hasInit");
        attributes.add("init");
        attributes.add("isParam");
        attributes.add("storageClass");
    }

    /**
     * 
     */
    @Override
    protected void fillWithSelects(List<String> selects) {
        this.aNamedDecl.fillWithSelects(selects);
        selects.add("init");
    }

    /**
     * 
     */
    @Override
    protected void fillWithActions(List<String> actions) {
        this.aNamedDecl.fillWithActions(actions);
        actions.add("void setInit(joinpoint)");
        actions.add("void setInit(String)");
    }

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public String get_class() {
        return "vardecl";
    }

    /**
     * Defines if this joinpoint is an instanceof a given joinpoint class
     * @return True if this join point is an instanceof the given class
     */
    @Override
    public boolean instanceOf(String joinpointClass) {
        boolean isInstance = get_class().equals(joinpointClass);
        if(isInstance) {
        	return true;
        }
        return this.aNamedDecl.instanceOf(joinpointClass);
    }
    /**
     * 
     */
    protected enum VardeclAttributes {
        HASINIT("hasInit"),
        INIT("init"),
        ISPARAM("isParam"),
        STORAGECLASS("storageClass"),
        NAME("name"),
        ISPUBLIC("isPublic"),
        PARENT("parent"),
        ASTANCESTOR("astAncestor"),
        AST("ast"),
        CODE("code"),
        ISINSIDELOOPHEADER("isInsideLoopHeader"),
        LINE("line"),
        DESCENDANTSANDSELF("descendantsAndSelf"),
        ASTNUMCHILDREN("astNumChildren"),
        TYPE("type"),
        DESCENDANTS("descendants"),
        ASTCHILDREN("astChildren"),
        ROOT("root"),
        JAVAVALUE("javaValue"),
        CHAINANCESTOR("chainAncestor"),
        CHAIN("chain"),
        JOINPOINTTYPE("joinpointType"),
        CURRENTREGION("currentRegion"),
        ANCESTOR("ancestor"),
        HASASTPARENT("hasAstParent"),
        ASTCHILD("astChild"),
        PARENTREGION("parentRegion"),
        ASTNAME("astName"),
        ASTID("astId"),
        CONTAINS("contains"),
        JAVAFIELDS("javaFields"),
        ASTPARENT("astParent"),
        JAVAFIELDTYPE("javaFieldType"),
        USERFIELD("userField"),
        LOCATION("location"),
        HASNODE("hasNode"),
        GETUSERFIELD("getUserField"),
        HASPARENT("hasParent");
        private String name;

        /**
         * 
         */
        private VardeclAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<VardeclAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(VardeclAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
