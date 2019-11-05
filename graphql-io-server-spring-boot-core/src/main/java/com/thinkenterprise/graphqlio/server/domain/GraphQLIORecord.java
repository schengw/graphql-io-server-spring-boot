package com.thinkenterprise.graphqlio.server.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphQLIORecord {

	
	static final String REGEXPPATTERN = "^(?:(.+?)#(.+?)\\.(.+?)->)?(.+?)\\((.+?)\\)->(.+?)#\\{(.*?)\\}\\.\\{(.+?)\\}$";
	
    // Introduce GraphQLIOOperationType 
	public enum GraphQLIOOperationType {

		INVALID(""),
		READ ("read"),
		CREATE ("create"),
		UPDATE ("update"),
		DELETE ("delete");
	
		private String op;
		
		private GraphQLIOOperationType( String op) {
			this.set(op);
		}
		
		public String op() {
			return this.op;
		}

		void set(String op) {
			if ( op.equals("read") || 
				 op.equals("create") || 
				 op.equals("update") || 
				 op.equals("delete") )	
				this.op = op;
			else
				this.op = "";
		}

	}

	
    // Introduce GraphQLIOArityType 
	public enum GraphQLIOArityType {
		
		INVALID(""),
		ONE ("one"),
		MANY("many"),
		ALL ("all");

		private String arity;
		
		private GraphQLIOArityType(String arity) {
			this.set(arity);
		}
		
		public String arity() {
			return arity;
		}
		
		void set( String arity) {
			if ( arity.equals("one")  ||  arity.equals("many") ||  arity.equals("all") )
				this.arity = arity;
			else
				this.arity = "";
		}
		
	}
		
	
	private String srcType = null; 
	private String srcId = null; 
	private String srcAttr = null; 
	private GraphQLIOOperationType op = GraphQLIOOperationType.READ; 	
	private GraphQLIOArityType arity = GraphQLIOArityType.ONE;
	private String dstType = "";
	private String dstIds[] = null;
	private String dstAttrs[] = null;


    // Introduce Getter;  Setter not required, as we use Builder 

	public String srcType() {
		return srcType; 
	}
	
	public String srcId() {
		return srcId; 
	}
	
	public String srcAttr() {
		return srcAttr; 
	}

	public GraphQLIOOperationType op() {
		return op; 
	}
	
	public GraphQLIOArityType arity() {
		return arity; 
	}

	public String dstType() {
		return dstType; 
	}
	
	public String[] dstIds() {
		return dstIds; 
	}
	
	public String[] dstAttrs() {
		return dstAttrs; 
	}

		
	@Override
	public String toString() {
		return 	"<GraphQLIORecord> (" + 
				" srcType = " + srcType + 
				", srcId = " + srcId + 
				", srcAttr = " + srcAttr + 
				", op = " + op + 
				", arity = " + arity + 
				", dstType = " + dstType + 
				", dstAttrs = " + "[" + String.join(",", dstAttrs) + "]" +
				", dstIds = " + "[" + String.join(",", dstIds) + "]" +
				")";
	}
		
	private boolean compareString( String str1, String str2) {
		
		if (str1 == str2) return true;
		if (str1 != null) {  // both != null
			return str1.equals(str2);
		}
		else 
			return true;  /// both null
	}
	
	private boolean compare( String array1[], String array2[]) {

		if ( array1 == array2) return true;
		if ( array1 != null ) {
			if (array1.length != array2.length )
				return false;
			else {
				boolean isEqual = true;
				for (int i=0; isEqual && i < array1.length; i++ ) {
					isEqual = compareString(array1[i], array2[i]);					
				}
				return isEqual;
			}
		}
		else
			return true;	/// both null
	}
	
	@Override
	public boolean equals( Object o) {
		if (this == o) return true;	/// reference to same object
		if ( !(o instanceof GraphQLIORecord) ) return false;
		GraphQLIORecord r = (GraphQLIORecord)o;
		return 	( 	compareString(this.srcType,r.srcType)	&& 
					compareString(this.srcId, r.srcId)		&&  
					compareString(this.srcAttr, r.srcAttr)	&&
					compareString(this.op.op(), r.op.op())	&&
					compareString(this.arity.arity(), r.arity.arity())		&&
					compareString(this.dstType, r.dstType)	&&
					compare(this.dstAttrs, r.dstAttrs)		&&
					compare(this.dstIds, r.dstIds)	
				);
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		
		result = 31 * result + (srcType != null ? srcType.hashCode() : 0);
		result = 31 * result + (srcId != null ? srcId.hashCode() : 0);
		result = 31 * result + (srcAttr != null ? srcAttr.hashCode() : 0);
		result = 31 * result + (op != null ? op.hashCode() : 0);
		result = 31 * result + (arity != null ? arity.hashCode() : 0);
		result = 31 * result + (dstType != null ? dstType.hashCode() : 0);
		result = 31 * result + (dstAttrs != null ? dstAttrs.hashCode() : 0);
		result = 31 * result + (dstIds != null ? dstIds.hashCode() : 0);
		
		return result;
	}
		
	
	//// stringify GraphQLIORecord	
	//// sample format: srcType#srcId.srcAttr->op(arity)->dstType#{dstId1,dstId2}.{dstAttr1,dstAttr2}
	public String stringify() {
		String str = "";
		
		/// optional attributes
		/// srcType#srcId.srcAttr
		if ( srcType != null &&  srcId != null  &&  srcAttr != null) {
			str += srcType + "#" + srcId + "." + srcAttr + "->";
		}
		
		/// manadatory attributes
		/// op(arity)->dstType#{dstId1,dstId2}.{dstAttr1,dstAttr2}
		str += op.op() + "(" + arity.arity() + ")->";
		str += dstType + "#";

		/// comma separated array of dstIds
		str += "{" + String.join(",", dstIds) + "}";
		str += ".";
		/// comma separated array of dstAttrs
		str += "{" + String.join(",",dstAttrs) + "}";
		
		return str;
	}

	/// check if String matches predefined regular Expression
	public static boolean matchesPredefinedPattern(String strRecord) {
		return strRecord.matches(REGEXPPATTERN);		
	}
	
	
	// Introduce Builder Pattern 
	private GraphQLIORecord (GraphQLIORecordBuilder builder) {

		this.srcType = builder.srcType;
		this.srcId = builder.srcId;
		this.srcAttr = builder.srcAttr;
		this.op = builder.op;
		this.arity = builder.arity;
		this.dstType = builder.dstType;
		this.dstAttrs = builder.dstAttrs;
		this.dstIds = builder.dstIds;
	}
	
    public static GraphQLIORecordBuilder builder() {
		return new GraphQLIORecordBuilder();
	}

 
    /// Builder for GraphQLIORecord
	public static final class GraphQLIORecordBuilder {

		// optional attributes 
		private String srcType = null; 
		private String srcId = null; 
		private String srcAttr = null; 

		/// mandatory attributes
		private GraphQLIOOperationType op = GraphQLIOOperationType.READ; 	
		private GraphQLIOArityType arity = GraphQLIOArityType.ONE;
		private String dstType = "";
		private String dstIds[] = null;
		private String dstAttrs[] = null;
		
		/// create record from string
		private String stringifiedRecord = null;
	
		public GraphQLIORecordBuilder () {	
		}
		
		public GraphQLIORecordBuilder (String stringifiedRecord) {	
			
		}

		/// constructor with all mandatory fields
		public GraphQLIORecordBuilder( String op, String arity, String dstType, String dstAttrs[], String dstIds[] )
		{
			this.op.set(op);
			this.arity.set(arity);
			this.dstType = dstType;
			this.dstAttrs = dstAttrs;
			this.dstIds = dstIds;			
		}

		public GraphQLIORecordBuilder srcType(String srcType ) {
			this.srcType = srcType;
			return this;
		}

		public GraphQLIORecordBuilder srcId(String srcId ) {
			this.srcId = srcId;
			return this;
		}

		public GraphQLIORecordBuilder srcAttr(String srcAttr ) {
			this.srcAttr = srcAttr;
			return this;
		}
				
		public GraphQLIORecordBuilder op(String op ) {
			this.op.set(op);
			return this;
		}

		public GraphQLIORecordBuilder arity(String arity ) {
			this.arity.set(arity);
			return this;
		}
		
		public GraphQLIORecordBuilder dstType(String dstType ) {
			this.dstType = dstType;
			return this;
		}

		public GraphQLIORecordBuilder dstAttrs(String dstAttrs[] ) {
			this.dstAttrs = dstAttrs;
			return this;
		}
		
		public GraphQLIORecordBuilder dstIds(String dstIds[] ) {
			this.dstIds = dstIds;
			return this;
		}

		public GraphQLIORecordBuilder stringified(String stringifiedRecord ) {
			this.stringifiedRecord = stringifiedRecord;
			return this;
		}

		
		public GraphQLIORecord build() {
			
			if ( this.stringifiedRecord != null) {
				unstringify(stringifiedRecord);
			}
			return new GraphQLIORecord(this);
		}

		
		//// unstringify from record		
		private void unstringify( String strRecord) {
			
			// use pattern defined in https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
			if (strRecord.matches(REGEXPPATTERN)) {
			
		        Pattern pattern = Pattern.compile(REGEXPPATTERN);
		        Matcher matcher = pattern.matcher(strRecord);
		        while (matcher.find()) {
	
		        	srcType(matcher.group(1)); 
		        	srcId(matcher.group(2)); 
		        	srcAttr(matcher.group(3));
		        	op(matcher.group(4)); 	
		        	arity(matcher.group(5));
		        	dstType(matcher.group(6));
		        	dstIds(matcher.group(7).split("\\,"));
		        	dstAttrs(matcher.group(8).split("\\,"));	        	
		        }		        
			}
			
		}
				
	}
   
}
