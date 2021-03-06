package pt.up.fe.specs.clava.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.JoinPoint;
import pt.up.fe.specs.clava.ClavaNode;
import java.util.List;
import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.ActionException;
import java.util.Map;
import org.lara.interpreter.exception.AttributeException;
import javax.script.Bindings;
import pt.up.fe.specs.clava.weaver.CxxWeaver;

/**
 * Abstract class containing the global attributes and default action exception.
 * This class is overwritten when the weaver generator is executed.
 * @author Lara Weaver Generator
 */
public abstract class AJoinPoint extends JoinPoint {

    /**
     * 
     */
    @Override
    public boolean same(JoinPoint iJoinPoint) {
        if (this.get_class().equals(iJoinPoint.get_class())) {
        
                return this.compareNodes((AJoinPoint) iJoinPoint);
            }
            return false;
    }

    /**
     * Compares the two join points based on their node reference of the used compiler/parsing tool.<br>
     * This is the default implementation for comparing two join points. <br>
     * <b>Note for developers:</b> A weaver may override this implementation in the editable abstract join point, so
     * the changes are made for all join points, or override this method in specific join points.
     */
    public boolean compareNodes(AJoinPoint aJoinPoint) {
        return this.getNode().equals(aJoinPoint.getNode());
    }

    /**
     * Returns the tree node reference of this join point.<br><b>NOTE</b>This method is essentially used to compare two join points
     * @return Tree node reference
     */
    public abstract ClavaNode getNode();

    /**
     * 
     */
    @Override
    public void defImpl(String attribute, Object value) {
        switch(attribute){
        default: throw new UnsupportedOperationException("Join point "+get_class()+": attribute '"+attribute+"' cannot be defined");
        }
    }

    /**
     * 
     */
    @Override
    protected void fillWithActions(List<String> actions) {
        actions.add("replaceWith(AJoinPoint node)");
        actions.add("insertBefore(AJoinPoint node)");
        actions.add("insertBefore(String node)");
        actions.add("insertAfter(AJoinPoint node)");
        actions.add("insertAfter(String code)");
        actions.add("detach()");
        actions.add("setType(AJoinPoint type)");
        actions.add("copy()");
        actions.add("setUserField(String fieldName, Object value)");
        actions.add("setUserField(Map<?, ?> fieldNameAndValue)");
        actions.add("messageToUser(String message)");
    }

    /**
     * 
     * @param node 
     */
    public AJoinPoint replaceWithImpl(AJoinPoint node) {
        throw new UnsupportedOperationException(get_class()+": Action replaceWith not implemented ");
    }

    /**
     * 
     * @param node 
     */
    public final AJoinPoint replaceWith(AJoinPoint node) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "replaceWith", this, Optional.empty(), node);
        	}
        	AJoinPoint result = this.replaceWithImpl(node);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "replaceWith", this, Optional.ofNullable(result), node);
        	}
        	return result;
        } catch(Exception e) {
        	throw new ActionException(get_class(), "replaceWith", e);
        }
    }

    /**
     * 
     * @param node 
     */
    public AJoinPoint insertBeforeImpl(AJoinPoint node) {
        throw new UnsupportedOperationException(get_class()+": Action insertBefore not implemented ");
    }

    /**
     * 
     * @param node 
     */
    public final AJoinPoint insertBefore(AJoinPoint node) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "insertBefore", this, Optional.empty(), node);
        	}
        	AJoinPoint result = this.insertBeforeImpl(node);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "insertBefore", this, Optional.ofNullable(result), node);
        	}
        	return result;
        } catch(Exception e) {
        	throw new ActionException(get_class(), "insertBefore", e);
        }
    }

    /**
     * 
     * @param node 
     */
    public AJoinPoint insertBeforeImpl(String node) {
        throw new UnsupportedOperationException(get_class()+": Action insertBefore not implemented ");
    }

    /**
     * 
     * @param node 
     */
    public final AJoinPoint insertBefore(String node) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "insertBefore", this, Optional.empty(), node);
        	}
        	AJoinPoint result = this.insertBeforeImpl(node);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "insertBefore", this, Optional.ofNullable(result), node);
        	}
        	return result;
        } catch(Exception e) {
        	throw new ActionException(get_class(), "insertBefore", e);
        }
    }

    /**
     * 
     * @param node 
     */
    public AJoinPoint insertAfterImpl(AJoinPoint node) {
        throw new UnsupportedOperationException(get_class()+": Action insertAfter not implemented ");
    }

    /**
     * 
     * @param node 
     */
    public final AJoinPoint insertAfter(AJoinPoint node) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "insertAfter", this, Optional.empty(), node);
        	}
        	AJoinPoint result = this.insertAfterImpl(node);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "insertAfter", this, Optional.ofNullable(result), node);
        	}
        	return result;
        } catch(Exception e) {
        	throw new ActionException(get_class(), "insertAfter", e);
        }
    }

    /**
     * 
     * @param code 
     */
    public AJoinPoint insertAfterImpl(String code) {
        throw new UnsupportedOperationException(get_class()+": Action insertAfter not implemented ");
    }

    /**
     * 
     * @param code 
     */
    public final AJoinPoint insertAfter(String code) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "insertAfter", this, Optional.empty(), code);
        	}
        	AJoinPoint result = this.insertAfterImpl(code);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "insertAfter", this, Optional.ofNullable(result), code);
        	}
        	return result;
        } catch(Exception e) {
        	throw new ActionException(get_class(), "insertAfter", e);
        }
    }

    /**
     * 
     */
    public void detachImpl() {
        throw new UnsupportedOperationException(get_class()+": Action detach not implemented ");
    }

    /**
     * 
     */
    public final void detach() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "detach", this, Optional.empty());
        	}
        	this.detachImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "detach", this, Optional.empty());
        	}
        } catch(Exception e) {
        	throw new ActionException(get_class(), "detach", e);
        }
    }

    /**
     * 
     * @param type 
     */
    public void setTypeImpl(AJoinPoint type) {
        throw new UnsupportedOperationException(get_class()+": Action setType not implemented ");
    }

    /**
     * 
     * @param type 
     */
    public final void setType(AJoinPoint type) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "setType", this, Optional.empty(), type);
        	}
        	this.setTypeImpl(type);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "setType", this, Optional.empty(), type);
        	}
        } catch(Exception e) {
        	throw new ActionException(get_class(), "setType", e);
        }
    }

    /**
     * 
     */
    public AJoinPoint copyImpl() {
        throw new UnsupportedOperationException(get_class()+": Action copy not implemented ");
    }

    /**
     * 
     */
    public final AJoinPoint copy() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "copy", this, Optional.empty());
        	}
        	AJoinPoint result = this.copyImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "copy", this, Optional.ofNullable(result));
        	}
        	return result;
        } catch(Exception e) {
        	throw new ActionException(get_class(), "copy", e);
        }
    }

    /**
     * 
     * @param fieldName 
     * @param value 
     */
    public Object setUserFieldImpl(String fieldName, Object value) {
        throw new UnsupportedOperationException(get_class()+": Action setUserField not implemented ");
    }

    /**
     * 
     * @param fieldName 
     * @param value 
     */
    public final Object setUserField(String fieldName, Object value) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "setUserField", this, Optional.empty(), fieldName, value);
        	}
        	Object result = this.setUserFieldImpl(fieldName, value);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "setUserField", this, Optional.ofNullable(result), fieldName, value);
        	}
        	return result;
        } catch(Exception e) {
        	throw new ActionException(get_class(), "setUserField", e);
        }
    }

    /**
     * 
     * @param fieldNameAndValue 
     */
    public Object setUserFieldImpl(Map<?, ?> fieldNameAndValue) {
        throw new UnsupportedOperationException(get_class()+": Action setUserField not implemented ");
    }

    /**
     * 
     * @param fieldNameAndValue 
     */
    public final Object setUserField(Map<?, ?> fieldNameAndValue) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "setUserField", this, Optional.empty(), fieldNameAndValue);
        	}
        	Object result = this.setUserFieldImpl(fieldNameAndValue);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "setUserField", this, Optional.ofNullable(result), fieldNameAndValue);
        	}
        	return result;
        } catch(Exception e) {
        	throw new ActionException(get_class(), "setUserField", e);
        }
    }

    /**
     * 
     * @param message 
     */
    public void messageToUserImpl(String message) {
        throw new UnsupportedOperationException(get_class()+": Action messageToUser not implemented ");
    }

    /**
     * 
     * @param message 
     */
    public final void messageToUser(String message) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "messageToUser", this, Optional.empty(), message);
        	}
        	this.messageToUserImpl(message);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "messageToUser", this, Optional.empty(), message);
        	}
        } catch(Exception e) {
        	throw new ActionException(get_class(), "messageToUser", e);
        }
    }

    /**
     * 
     */
    @Override
    protected void fillWithAttributes(List<String> attributes) {
        //Attributes available for all join points
        attributes.add("root");
        attributes.add("parent");
        attributes.add("ancestor(String type)");
        attributes.add("descendants(String type)");
        attributes.add("descendantsAndSelf(String type)");
        attributes.add("chainAncestor(String type)");
        attributes.add("astParent");
        attributes.add("astAncestor(String type)");
        attributes.add("contains(AJoinPoint jp)");
        attributes.add("hasParent");
        attributes.add("hasAstParent");
        attributes.add("line");
        attributes.add("location");
        attributes.add("astId");
        attributes.add("ast");
        attributes.add("code");
        attributes.add("joinpointType");
        attributes.add("type");
        attributes.add("astNumChildren");
        attributes.add("astChildren");
        attributes.add("astName");
        attributes.add("astChild(Integer index)");
        attributes.add("hasNode(Object nodeOrJp)");
        attributes.add("chain");
        attributes.add("javaFields");
        attributes.add("javaValue(String fieldName)");
        attributes.add("javaFieldType(String fieldName)");
        attributes.add("isInsideLoopHeader");
        attributes.add("getUserField(String fieldName)");
        attributes.add("userField(String fieldName)");
        attributes.add("parentRegion");
        attributes.add("currentRegion");
    }

    /**
     * Returns the 'program' joinpoint
     */
    public abstract AJoinPoint getRootImpl();

    /**
     * Returns the 'program' joinpoint
     */
    public final Object getRoot() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "root", Optional.empty());
        	}
        	AJoinPoint result = this.getRootImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "root", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "root", e);
        }
    }

    /**
     * Get value on attribute parent
     * @return the attribute's value
     */
    public abstract AJoinPoint getParentImpl();

    /**
     * Get value on attribute parent
     * @return the attribute's value
     */
    public final Object getParent() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "parent", Optional.empty());
        	}
        	AJoinPoint result = this.getParentImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "parent", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "parent", e);
        }
    }

    /**
     * 
     * @param type
     * @return 
     */
    public abstract AJoinPoint ancestorImpl(String type);

    /**
     * 
     * @param type
     * @return 
     */
    public final Object ancestor(String type) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "ancestor", Optional.empty(), type);
        	}
        	AJoinPoint result = this.ancestorImpl(type);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "ancestor", Optional.ofNullable(result), type);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "ancestor", e);
        }
    }

    /**
     * 
     * @param type
     * @return 
     */
    public abstract AJoinPoint[] descendantsArrayImpl(String type);

    /**
     * 
     * @param type
     * @return 
     */
    public Bindings descendantsImpl(String type) {
        AJoinPoint[] aJoinPointArrayImpl0 = descendantsArrayImpl(type);
        Bindings nativeArray0 = getWeaverEngine().getScriptEngine().toNativeArray(aJoinPointArrayImpl0);
        return nativeArray0;
    }

    /**
     * 
     * @param type
     * @return 
     */
    public final Object descendants(String type) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "descendants", Optional.empty(), type);
        	}
        	Bindings result = this.descendantsImpl(type);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "descendants", Optional.ofNullable(result), type);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "descendants", e);
        }
    }

    /**
     * 
     * @param type
     * @return 
     */
    public abstract AJoinPoint[] descendantsAndSelfArrayImpl(String type);

    /**
     * 
     * @param type
     * @return 
     */
    public Bindings descendantsAndSelfImpl(String type) {
        AJoinPoint[] aJoinPointArrayImpl0 = descendantsAndSelfArrayImpl(type);
        Bindings nativeArray0 = getWeaverEngine().getScriptEngine().toNativeArray(aJoinPointArrayImpl0);
        return nativeArray0;
    }

    /**
     * 
     * @param type
     * @return 
     */
    public final Object descendantsAndSelf(String type) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "descendantsAndSelf", Optional.empty(), type);
        	}
        	Bindings result = this.descendantsAndSelfImpl(type);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "descendantsAndSelf", Optional.ofNullable(result), type);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "descendantsAndSelf", e);
        }
    }

    /**
     * 
     * @param type
     * @return 
     */
    public abstract AJoinPoint chainAncestorImpl(String type);

    /**
     * 
     * @param type
     * @return 
     */
    public final Object chainAncestor(String type) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "chainAncestor", Optional.empty(), type);
        	}
        	AJoinPoint result = this.chainAncestorImpl(type);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "chainAncestor", Optional.ofNullable(result), type);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "chainAncestor", e);
        }
    }

    /**
     * Get value on attribute astParent
     * @return the attribute's value
     */
    public abstract AJoinPoint getAstParentImpl();

    /**
     * Get value on attribute astParent
     * @return the attribute's value
     */
    public final Object getAstParent() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "astParent", Optional.empty());
        	}
        	AJoinPoint result = this.getAstParentImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "astParent", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "astParent", e);
        }
    }

    /**
     * 
     * @param type
     * @return 
     */
    public abstract AJoinPoint astAncestorImpl(String type);

    /**
     * 
     * @param type
     * @return 
     */
    public final Object astAncestor(String type) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "astAncestor", Optional.empty(), type);
        	}
        	AJoinPoint result = this.astAncestorImpl(type);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "astAncestor", Optional.ofNullable(result), type);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "astAncestor", e);
        }
    }

    /**
     * 
     * @param jp
     * @return 
     */
    public abstract Boolean containsImpl(AJoinPoint jp);

    /**
     * 
     * @param jp
     * @return 
     */
    public final Object contains(AJoinPoint jp) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "contains", Optional.empty(), jp);
        	}
        	Boolean result = this.containsImpl(jp);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "contains", Optional.ofNullable(result), jp);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "contains", e);
        }
    }

    /**
     * Get value on attribute hasParent
     * @return the attribute's value
     */
    public abstract Boolean getHasParentImpl();

    /**
     * Get value on attribute hasParent
     * @return the attribute's value
     */
    public final Object getHasParent() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "hasParent", Optional.empty());
        	}
        	Boolean result = this.getHasParentImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "hasParent", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "hasParent", e);
        }
    }

    /**
     * Get value on attribute hasAstParent
     * @return the attribute's value
     */
    public abstract Boolean getHasAstParentImpl();

    /**
     * Get value on attribute hasAstParent
     * @return the attribute's value
     */
    public final Object getHasAstParent() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "hasAstParent", Optional.empty());
        	}
        	Boolean result = this.getHasAstParentImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "hasAstParent", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "hasAstParent", e);
        }
    }

    /**
     * Get value on attribute line
     * @return the attribute's value
     */
    public abstract Integer getLineImpl();

    /**
     * Get value on attribute line
     * @return the attribute's value
     */
    public final Object getLine() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "line", Optional.empty());
        	}
        	Integer result = this.getLineImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "line", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "line", e);
        }
    }

    /**
     * Get value on attribute location
     * @return the attribute's value
     */
    public abstract String getLocationImpl();

    /**
     * Get value on attribute location
     * @return the attribute's value
     */
    public final Object getLocation() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "location", Optional.empty());
        	}
        	String result = this.getLocationImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "location", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "location", e);
        }
    }

    /**
     * Get value on attribute astId
     * @return the attribute's value
     */
    public abstract String getAstIdImpl();

    /**
     * Get value on attribute astId
     * @return the attribute's value
     */
    public final Object getAstId() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "astId", Optional.empty());
        	}
        	String result = this.getAstIdImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "astId", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "astId", e);
        }
    }

    /**
     * Get value on attribute ast
     * @return the attribute's value
     */
    public abstract String getAstImpl();

    /**
     * Get value on attribute ast
     * @return the attribute's value
     */
    public final Object getAst() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "ast", Optional.empty());
        	}
        	String result = this.getAstImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "ast", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "ast", e);
        }
    }

    /**
     * Get value on attribute code
     * @return the attribute's value
     */
    public abstract String getCodeImpl();

    /**
     * Get value on attribute code
     * @return the attribute's value
     */
    public final Object getCode() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "code", Optional.empty());
        	}
        	String result = this.getCodeImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "code", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "code", e);
        }
    }

    /**
     * Get value on attribute joinpointType
     * @return the attribute's value
     */
    public abstract String getJoinpointTypeImpl();

    /**
     * Get value on attribute joinpointType
     * @return the attribute's value
     */
    public final Object getJoinpointType() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "joinpointType", Optional.empty());
        	}
        	String result = this.getJoinpointTypeImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "joinpointType", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "joinpointType", e);
        }
    }

    /**
     * Get value on attribute type
     * @return the attribute's value
     */
    public abstract AJoinPoint getTypeImpl();

    /**
     * Get value on attribute type
     * @return the attribute's value
     */
    public final Object getType() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "type", Optional.empty());
        	}
        	AJoinPoint result = this.getTypeImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "type", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "type", e);
        }
    }

    /**
     * Get value on attribute astNumChildren
     * @return the attribute's value
     */
    public abstract Integer getAstNumChildrenImpl();

    /**
     * Get value on attribute astNumChildren
     * @return the attribute's value
     */
    public final Object getAstNumChildren() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "astNumChildren", Optional.empty());
        	}
        	Integer result = this.getAstNumChildrenImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "astNumChildren", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "astNumChildren", e);
        }
    }

    /**
     * Get value on attribute astChildren
     * @return the attribute's value
     */
    public abstract AJoinPoint[] getAstChildrenArrayImpl();

    /**
     * Get value on attribute astChildren
     * @return the attribute's value
     */
    public Bindings getAstChildrenImpl() {
        AJoinPoint[] aJoinPointArrayImpl0 = getAstChildrenArrayImpl();
        Bindings nativeArray0 = getWeaverEngine().getScriptEngine().toNativeArray(aJoinPointArrayImpl0);
        return nativeArray0;
    }

    /**
     * Get value on attribute astChildren
     * @return the attribute's value
     */
    public final Object getAstChildren() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "astChildren", Optional.empty());
        	}
        	Bindings result = this.getAstChildrenImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "astChildren", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "astChildren", e);
        }
    }

    /**
     * Get value on attribute astName
     * @return the attribute's value
     */
    public abstract String getAstNameImpl();

    /**
     * Get value on attribute astName
     * @return the attribute's value
     */
    public final Object getAstName() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "astName", Optional.empty());
        	}
        	String result = this.getAstNameImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "astName", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "astName", e);
        }
    }

    /**
     * 
     * @param index
     * @return 
     */
    public abstract AJoinPoint astChildImpl(Integer index);

    /**
     * 
     * @param index
     * @return 
     */
    public final Object astChild(Integer index) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "astChild", Optional.empty(), index);
        	}
        	AJoinPoint result = this.astChildImpl(index);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "astChild", Optional.ofNullable(result), index);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "astChild", e);
        }
    }

    /**
     * 
     * @param nodeOrJp
     * @return 
     */
    public abstract Boolean hasNodeImpl(Object nodeOrJp);

    /**
     * 
     * @param nodeOrJp
     * @return 
     */
    public final Object hasNode(Object nodeOrJp) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "hasNode", Optional.empty(), nodeOrJp);
        	}
        	Boolean result = this.hasNodeImpl(nodeOrJp);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "hasNode", Optional.ofNullable(result), nodeOrJp);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "hasNode", e);
        }
    }

    /**
     * Get value on attribute chain
     * @return the attribute's value
     */
    public abstract String[] getChainArrayImpl();

    /**
     * Get value on attribute chain
     * @return the attribute's value
     */
    public Bindings getChainImpl() {
        String[] stringArrayImpl0 = getChainArrayImpl();
        Bindings nativeArray0 = getWeaverEngine().getScriptEngine().toNativeArray(stringArrayImpl0);
        return nativeArray0;
    }

    /**
     * Get value on attribute chain
     * @return the attribute's value
     */
    public final Object getChain() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "chain", Optional.empty());
        	}
        	Bindings result = this.getChainImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "chain", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "chain", e);
        }
    }

    /**
     * Get value on attribute javaFields
     * @return the attribute's value
     */
    public abstract String[] getJavaFieldsArrayImpl();

    /**
     * Get value on attribute javaFields
     * @return the attribute's value
     */
    public Bindings getJavaFieldsImpl() {
        String[] stringArrayImpl0 = getJavaFieldsArrayImpl();
        Bindings nativeArray0 = getWeaverEngine().getScriptEngine().toNativeArray(stringArrayImpl0);
        return nativeArray0;
    }

    /**
     * Get value on attribute javaFields
     * @return the attribute's value
     */
    public final Object getJavaFields() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "javaFields", Optional.empty());
        	}
        	Bindings result = this.getJavaFieldsImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "javaFields", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "javaFields", e);
        }
    }

    /**
     * 
     * @param fieldName
     * @return 
     */
    public abstract Object javaValueImpl(String fieldName);

    /**
     * 
     * @param fieldName
     * @return 
     */
    public final Object javaValue(String fieldName) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "javaValue", Optional.empty(), fieldName);
        	}
        	Object result = this.javaValueImpl(fieldName);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "javaValue", Optional.ofNullable(result), fieldName);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "javaValue", e);
        }
    }

    /**
     * 
     * @param fieldName
     * @return 
     */
    public abstract String javaFieldTypeImpl(String fieldName);

    /**
     * 
     * @param fieldName
     * @return 
     */
    public final Object javaFieldType(String fieldName) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "javaFieldType", Optional.empty(), fieldName);
        	}
        	String result = this.javaFieldTypeImpl(fieldName);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "javaFieldType", Optional.ofNullable(result), fieldName);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "javaFieldType", e);
        }
    }

    /**
     * Get value on attribute isInsideLoopHeader
     * @return the attribute's value
     */
    public abstract Boolean getIsInsideLoopHeaderImpl();

    /**
     * Get value on attribute isInsideLoopHeader
     * @return the attribute's value
     */
    public final Object getIsInsideLoopHeader() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "isInsideLoopHeader", Optional.empty());
        	}
        	Boolean result = this.getIsInsideLoopHeaderImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "isInsideLoopHeader", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "isInsideLoopHeader", e);
        }
    }

    /**
     * 
     * @param fieldName
     * @return 
     */
    public abstract Object getUserFieldImpl(String fieldName);

    /**
     * 
     * @param fieldName
     * @return 
     */
    public final Object getUserField(String fieldName) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "getUserField", Optional.empty(), fieldName);
        	}
        	Object result = this.getUserFieldImpl(fieldName);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "getUserField", Optional.ofNullable(result), fieldName);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "getUserField", e);
        }
    }

    /**
     * 
     * @param fieldName
     * @return 
     */
    public abstract Object userFieldImpl(String fieldName);

    /**
     * 
     * @param fieldName
     * @return 
     */
    public final Object userField(String fieldName) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "userField", Optional.empty(), fieldName);
        	}
        	Object result = this.userFieldImpl(fieldName);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "userField", Optional.ofNullable(result), fieldName);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "userField", e);
        }
    }

    /**
     * Get value on attribute parentRegion
     * @return the attribute's value
     */
    public abstract AJoinPoint getParentRegionImpl();

    /**
     * Get value on attribute parentRegion
     * @return the attribute's value
     */
    public final Object getParentRegion() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "parentRegion", Optional.empty());
        	}
        	AJoinPoint result = this.getParentRegionImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "parentRegion", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "parentRegion", e);
        }
    }

    /**
     * Get value on attribute currentRegion
     * @return the attribute's value
     */
    public abstract AJoinPoint getCurrentRegionImpl();

    /**
     * Get value on attribute currentRegion
     * @return the attribute's value
     */
    public final Object getCurrentRegion() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "currentRegion", Optional.empty());
        	}
        	AJoinPoint result = this.getCurrentRegionImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "currentRegion", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "currentRegion", e);
        }
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
        return super.instanceOf(joinpointClass);
    }

    /**
     * Returns the Weaving Engine this join point pertains to.
     */
    @Override
    public CxxWeaver getWeaverEngine() {
        return CxxWeaver.getCxxWeaver();
    }
}
