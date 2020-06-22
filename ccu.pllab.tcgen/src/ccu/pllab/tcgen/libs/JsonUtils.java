package ccu.pllab.tcgen.libs;

 
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {
	static private JSONObject ExtractLowestIDObj(JSONArray org) {
		int current_id = Integer.MAX_VALUE;
		int remove_id = -1;

		for (int i = 0; i < org.length(); i++) {
			JSONObject obj = org.optJSONObject(i);
			int id = Integer.valueOf(obj.optString("id"));
			if (id < current_id) {
				current_id = id;
				remove_id = i;
			}
		}

		JSONObject ret = org.optJSONObject(remove_id);
		org.remove(remove_id);

		return ret;
	}

	static public ImmutablePair<String, String> ParseNameTypePair(JSONObject obj) {
		String name = obj.optString("name").toString();
		String type = obj.optString("type").toString();

		ImmutablePair<String, String> pair = new ImmutablePair<String, String>(name, type);

		return pair;
	}

	// sorting the JSONArray from the "id" field,id start from 0
	static public JSONArray SortIDFile(JSONArray org) {
		JSONArray dst = new JSONArray();

		while (org.length() > 0) {
			JSONObject obj = ExtractLowestIDObj(org);
			dst.put(obj);
		}

		return dst;
	}
}
