package tw.com.useful.query;

public class GreaterThanQueryObject extends CompareQueryObject {
	
	private static final long serialVersionUID = 1L;

	public GreaterThanQueryObject(String field, Object value){
		super("$gt", field, value);
	}

}
