package tw.com.useful.query;

public class NotEqualQueryObject extends CompareQueryObject {
	
	private static final long serialVersionUID = 1L;

	public NotEqualQueryObject(String field, Object value){
		super("$ne", field, value);
	}

}
