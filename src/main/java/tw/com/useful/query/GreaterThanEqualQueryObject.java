package tw.com.useful.query;

public class GreaterThanEqualQueryObject extends CompareQueryObject {
	
	private static final long serialVersionUID = 1L;

	public GreaterThanEqualQueryObject(String field, Object value){
		super("$gte", field, value);
	}

}
