package tw.com.useful.data.model;


public class Category extends BaseDataModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Category(String code, String name){
		put("name", name);
		put("code", code);
	}
	
	public Category(){
		
	}

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}
	
	public String getCode() {
		return getString("code");
	}

	public void setCode(String code) {
		put("code", code);
	}
}
