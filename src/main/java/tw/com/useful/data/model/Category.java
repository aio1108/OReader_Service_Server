package tw.com.useful.data.model;


public class Category extends BaseDataModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Category(String id, String name){
		put("name", name);
		put("_id", id);
	}
	
	public Category(){
		
	}

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}
}
