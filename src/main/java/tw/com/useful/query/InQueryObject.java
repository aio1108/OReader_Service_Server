package tw.com.useful.query;

import java.util.List;

public class InQueryObject extends CompareQueryObject {
	
	private static final long serialVersionUID = 1L;

	public InQueryObject(String field, List value){
		super("$in", field, value);
	}

}
