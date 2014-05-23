package tw.com.useful.query;

import java.util.List;

public class NotInQueryObject extends CompareQueryObject {
	
	private static final long serialVersionUID = 1L;

	public NotInQueryObject(String field, List value){
		super("$nin", field, value);
	}

}
