package tw.com.useful.query;

public class ElementMatchQueryObject extends CompareQueryObject {
	
	private static final long serialVersionUID = 1L;

	public ElementMatchQueryObject(String field, QueryObject value){
		super("$elemMatch", field, value);
	}

}
