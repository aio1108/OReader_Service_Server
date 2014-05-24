package tw.com.useful.data.model;


public class ViewType extends BaseDataModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ViewType(String id, String name){
		put("name", name);
		put("_id", id);
	}
	
	public ViewType(){
		
	}

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}
}
