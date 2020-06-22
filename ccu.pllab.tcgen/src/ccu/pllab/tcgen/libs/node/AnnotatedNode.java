package ccu.pllab.tcgen.libs.node;

 
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AnnotatedNode implements INode {

	@Override
	abstract public long getId();

	@Override
	abstract public INode clone();

	private JSONObject attributes;

	public AnnotatedNode() {
		attributes = new JSONObject();
	}

	public final JSONObject getAttributes() {
		try {
			JSONObject return_value = new JSONObject(this.attributes.toString());
			return return_value;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public final String getAttribute(String key) {
		return this.attributes.optString(key);
	}
	
	public String getAtt(){
		return this.attributes.toString();
	}

	public final void setAttribute(String key, String str) {
		try {
			attributes.put(key, str);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public final void setAttributes(JSONObject attributes2) {
		try {
			attributes = new JSONObject(attributes2.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
