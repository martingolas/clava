package pt.up.fe.specs.clava.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.AttributeException;
import javax.script.Bindings;
import org.lara.interpreter.exception.ActionException;
import java.util.List;
import org.lara.interpreter.weaver.interf.JoinPoint;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * Auto-Generated class for join point AOmp
 * This class is overwritten by the Weaver Generator.
 * 
 * 
 * @author Lara Weaver Generator
 */
public abstract class AOmp extends APragma {

    private APragma aPragma;

    /**
     * 
     */
    public AOmp(APragma aPragma){
        this.aPragma = aPragma;
    }
    /**
     * The kind of the directive
     */
    public abstract String getKindImpl();

    /**
     * The kind of the directive
     */
    public final Object getKind() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "kind", Optional.empty());
        	}
        	String result = this.getKindImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "kind", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "kind", e);
        }
    }

    /**
     * An integer expression, or undefined if no 'num_threads' clause is defined
     */
    public abstract String getNumThreadsImpl();

    /**
     * An integer expression, or undefined if no 'num_threads' clause is defined
     */
    public final Object getNumThreads() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "numThreads", Optional.empty());
        	}
        	String result = this.getNumThreadsImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "numThreads", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "numThreads", e);
        }
    }

    /**
     * One of 'master', 'close' or 'spread', or undefined if no 'proc_bind' clause is defined
     */
    public abstract String getProcBindImpl();

    /**
     * One of 'master', 'close' or 'spread', or undefined if no 'proc_bind' clause is defined
     */
    public final Object getProcBind() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "procBind", Optional.empty());
        	}
        	String result = this.getProcBindImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "procBind", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "procBind", e);
        }
    }

    /**
     * Get value on attribute _private
     * @return the attribute's value
     */
    public abstract String[] getPrivateArrayImpl();

    /**
     * The variable names of all private clauses, or empty array if no private clause is defined
     */
    public Bindings getPrivateImpl() {
        String[] stringArrayImpl0 = getPrivateArrayImpl();
        Bindings nativeArray0 = getWeaverEngine().getScriptEngine().toNativeArray(stringArrayImpl0);
        return nativeArray0;
    }

    /**
     * The variable names of all private clauses, or empty array if no private clause is defined
     */
    public final Object getPrivate() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "private", Optional.empty());
        	}
        	Bindings result = this.getPrivateImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "private", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "private", e);
        }
    }

    /**
     * 
     * @param clauseName
     * @return 
     */
    public abstract Boolean hasClauseImpl(String clauseName);

    /**
     * 
     * @param clauseName
     * @return 
     */
    public final Object hasClause(String clauseName) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "hasClause", Optional.empty(), clauseName);
        	}
        	Boolean result = this.hasClauseImpl(clauseName);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "hasClause", Optional.ofNullable(result), clauseName);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "hasClause", e);
        }
    }

    /**
     * 
     * @param clauseName
     * @return 
     */
    public abstract Boolean isClauseLegalImpl(String clauseName);

    /**
     * 
     * @param clauseName
     * @return 
     */
    public final Object isClauseLegal(String clauseName) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "isClauseLegal", Optional.empty(), clauseName);
        	}
        	Boolean result = this.isClauseLegalImpl(clauseName);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "isClauseLegal", Optional.ofNullable(result), clauseName);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "isClauseLegal", e);
        }
    }

    /**
     * Get value on attribute clauseKinds
     * @return the attribute's value
     */
    public abstract String[] getClauseKindsArrayImpl();

    /**
     * The names of the kinds of all clauses in the pragma, or empty array if no clause is defined
     */
    public Bindings getClauseKindsImpl() {
        String[] stringArrayImpl0 = getClauseKindsArrayImpl();
        Bindings nativeArray0 = getWeaverEngine().getScriptEngine().toNativeArray(stringArrayImpl0);
        return nativeArray0;
    }

    /**
     * The names of the kinds of all clauses in the pragma, or empty array if no clause is defined
     */
    public final Object getClauseKinds() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "clauseKinds", Optional.empty());
        	}
        	Bindings result = this.getClauseKindsImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "clauseKinds", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "clauseKinds", e);
        }
    }

    /**
     * 
     * @param kind
     * @return 
     */
    public abstract String[] reductionArrayImpl(String kind);

    /**
     * 
     * @param kind
     * @return 
     */
    public Bindings reductionImpl(String kind) {
        String[] stringArrayImpl0 = reductionArrayImpl(kind);
        Bindings nativeArray0 = getWeaverEngine().getScriptEngine().toNativeArray(stringArrayImpl0);
        return nativeArray0;
    }

    /**
     * 
     * @param kind
     * @return 
     */
    public final Object reduction(String kind) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "reduction", Optional.empty(), kind);
        	}
        	Bindings result = this.reductionImpl(kind);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "reduction", Optional.ofNullable(result), kind);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "reduction", e);
        }
    }

    /**
     * Sets the value of the num_threads clause of an OpenMP pragma
     * @param newExpr 
     */
    public void setNumThreadsImpl(String newExpr) {
        throw new UnsupportedOperationException(get_class()+": Action setNumThreads not implemented ");
    }

    /**
     * Sets the value of the num_threads clause of an OpenMP pragma
     * @param newExpr 
     */
    public final void setNumThreads(String newExpr) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "setNumThreads", this, Optional.empty(), newExpr);
        	}
        	this.setNumThreadsImpl(newExpr);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "setNumThreads", this, Optional.empty(), newExpr);
        	}
        } catch(Exception e) {
        	throw new ActionException(get_class(), "setNumThreads", e);
        }
    }

    /**
     * Sets the value of the proc_bind clause of an OpenMP pragma
     * @param newBind 
     */
    public void setProcBindImpl(String newBind) {
        throw new UnsupportedOperationException(get_class()+": Action setProcBind not implemented ");
    }

    /**
     * Sets the value of the proc_bind clause of an OpenMP pragma
     * @param newBind 
     */
    public final void setProcBind(String newBind) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "setProcBind", this, Optional.empty(), newBind);
        	}
        	this.setProcBindImpl(newBind);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "setProcBind", this, Optional.empty(), newBind);
        	}
        } catch(Exception e) {
        	throw new ActionException(get_class(), "setProcBind", e);
        }
    }

    /**
     * Sets the variables of a private clause of an OpenMP pragma
     * @param newVariables 
     */
    public void setPrivateImpl(String[] newVariables) {
        throw new UnsupportedOperationException(get_class()+": Action setPrivate not implemented ");
    }

    /**
     * Sets the variables of a private clause of an OpenMP pragma
     * @param newVariables 
     */
    public final void setPrivate(String[] newVariables) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "setPrivate", this, Optional.empty(), newVariables);
        	}
        	this.setPrivateImpl(newVariables);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "setPrivate", this, Optional.empty(), newVariables);
        	}
        } catch(Exception e) {
        	throw new ActionException(get_class(), "setPrivate", e);
        }
    }

    /**
     * Sets the variables for a given kind of a reduction clause of an OpenMP pragma
     * @param kind 
     * @param newVariables 
     */
    public void setReductionImpl(String kind, String[] newVariables) {
        throw new UnsupportedOperationException(get_class()+": Action setReduction not implemented ");
    }

    /**
     * Sets the variables for a given kind of a reduction clause of an OpenMP pragma
     * @param kind 
     * @param newVariables 
     */
    public final void setReduction(String kind, String[] newVariables) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.BEGIN, "setReduction", this, Optional.empty(), kind, newVariables);
        	}
        	this.setReductionImpl(kind, newVariables);
        	if(hasListeners()) {
        		eventTrigger().triggerAction(Stage.END, "setReduction", this, Optional.empty(), kind, newVariables);
        	}
        } catch(Exception e) {
        	throw new ActionException(get_class(), "setReduction", e);
        }
    }

    /**
     * Get value on attribute name
     * @return the attribute's value
     */
    @Override
    public String getNameImpl() {
        return this.aPragma.getNameImpl();
    }

    /**
     * Get value on attribute target
     * @return the attribute's value
     */
    @Override
    public AJoinPoint getTargetImpl() {
        return this.aPragma.getTargetImpl();
    }

    /**
     * Get value on attribute content
     * @return the attribute's value
     */
    @Override
    public String getContentImpl() {
        return this.aPragma.getContentImpl();
    }

    /**
     * 
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithImpl(AJoinPoint node) {
        return this.aPragma.replaceWithImpl(node);
    }

    /**
     * 
     * @param node 
     */
    @Override
    public AJoinPoint insertBeforeImpl(AJoinPoint node) {
        return this.aPragma.insertBeforeImpl(node);
    }

    /**
     * 
     * @param node 
     */
    @Override
    public AJoinPoint insertBeforeImpl(String node) {
        return this.aPragma.insertBeforeImpl(node);
    }

    /**
     * 
     * @param node 
     */
    @Override
    public AJoinPoint insertAfterImpl(AJoinPoint node) {
        return this.aPragma.insertAfterImpl(node);
    }

    /**
     * 
     * @param code 
     */
    @Override
    public AJoinPoint insertAfterImpl(String code) {
        return this.aPragma.insertAfterImpl(code);
    }

    /**
     * 
     */
    @Override
    public void detachImpl() {
        this.aPragma.detachImpl();
    }

    /**
     * 
     * @param type 
     */
    @Override
    public void setTypeImpl(AJoinPoint type) {
        this.aPragma.setTypeImpl(type);
    }

    /**
     * 
     * @param message 
     */
    @Override
    public void messageToUserImpl(String message) {
        this.aPragma.messageToUserImpl(message);
    }

    /**
     * 
     * @param name 
     */
    @Override
    public void setNameImpl(String name) {
        this.aPragma.setNameImpl(name);
    }

    /**
     * 
     * @param content 
     */
    @Override
    public void setContentImpl(String content) {
        this.aPragma.setContentImpl(content);
    }

    /**
     * 
     * @param position 
     * @param code 
     */
    @Override
    public void insertImpl(String position, String code) {
        this.aPragma.insertImpl(position, code);
    }

    /**
     * 
     * @param attribute 
     * @param value 
     */
    @Override
    public void defImpl(String attribute, Object value) {
        this.aPragma.defImpl(attribute, value);
    }

    /**
     * 
     */
    @Override
    public String toString() {
        return this.aPragma.toString();
    }

    /**
     * 
     */
    @Override
    public Optional<? extends APragma> getSuper() {
        return Optional.of(this.aPragma);
    }

    /**
     * 
     */
    @Override
    public final List<? extends JoinPoint> select(String selectName) {
        List<? extends JoinPoint> joinPointList;
        switch(selectName) {
        	default:
        		joinPointList = this.aPragma.select(selectName);
        		break;
        }
        return joinPointList;
    }

    /**
     * 
     */
    @Override
    protected final void fillWithAttributes(List<String> attributes) {
        this.aPragma.fillWithAttributes(attributes);
        attributes.add("kind");
        attributes.add("numThreads");
        attributes.add("procBind");
        attributes.add("private");
        attributes.add("hasClause");
        attributes.add("isClauseLegal");
        attributes.add("clauseKinds");
        attributes.add("reduction");
    }

    /**
     * 
     */
    @Override
    protected final void fillWithSelects(List<String> selects) {
        this.aPragma.fillWithSelects(selects);
    }

    /**
     * 
     */
    @Override
    protected final void fillWithActions(List<String> actions) {
        this.aPragma.fillWithActions(actions);
        actions.add("void setNumThreads(String)");
        actions.add("void setProcBind(String)");
        actions.add("void setPrivate(String[])");
        actions.add("void setReduction(String, String[])");
    }

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "omp";
    }

    /**
     * Defines if this joinpoint is an instanceof a given joinpoint class
     * @return True if this join point is an instanceof the given class
     */
    @Override
    public final boolean instanceOf(String joinpointClass) {
        boolean isInstance = get_class().equals(joinpointClass);
        if(isInstance) {
        	return true;
        }
        return this.aPragma.instanceOf(joinpointClass);
    }
    /**
     * 
     */
    protected enum OmpAttributes {
        KIND("kind"),
        NUMTHREADS("numThreads"),
        PROCBIND("procBind"),
        PRIVATE("private"),
        HASCLAUSE("hasClause"),
        ISCLAUSELEGAL("isClauseLegal"),
        CLAUSEKINDS("clauseKinds"),
        REDUCTION("reduction"),
        NAME("name"),
        TARGET("target"),
        CONTENT("content"),
        PARENT("parent"),
        ASTANCESTOR("astAncestor"),
        AST("ast"),
        CODE("code"),
        ISINSIDELOOPHEADER("isInsideLoopHeader"),
        LINE("line"),
        ASTNUMCHILDREN("astNumChildren"),
        TYPE("type"),
        ROOT("root"),
        JAVAVALUE("javaValue"),
        CHAINANCESTOR("chainAncestor"),
        CHAIN("chain"),
        JOINPOINTTYPE("joinpointType"),
        CURRENTREGION("currentRegion"),
        ANCESTOR("ancestor"),
        ASTCHILD("astChild"),
        PARENTREGION("parentRegion"),
        ASTNAME("astName"),
        ASTID("astId"),
        CONTAINS("contains"),
        JAVAFIELDS("javaFields"),
        ASTPARENT("astParent"),
        SETUSERFIELD("setUserField"),
        JAVAFIELDTYPE("javaFieldType"),
        LOCATION("location"),
        GETUSERFIELD("getUserField"),
        HASPARENT("hasParent");
        private String name;

        /**
         * 
         */
        private OmpAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<OmpAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(OmpAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
