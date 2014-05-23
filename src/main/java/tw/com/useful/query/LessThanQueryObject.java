package tw.com.useful.query;

public class LessThanQueryObject extends CompareQueryObject {
	
	private static final long serialVersionUID = 1L;

	public LessThanQueryObject(String field, Object value){
		super("$lt", field, value);
	}

}
