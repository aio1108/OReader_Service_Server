package tw.com.useful.query;

public class LessThanEqualQueryObject extends CompareQueryObject {
	
	private static final long serialVersionUID = 1L;

	public LessThanEqualQueryObject(String field, Object value){
		super("$lte", field, value);
	}

}
