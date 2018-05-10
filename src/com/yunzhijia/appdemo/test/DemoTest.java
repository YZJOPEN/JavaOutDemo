
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunzhijia.appdemo.pojo.OutRecord;
import com.yunzhijia.appdemo.service.OutRecordService;

public class DemoTest extends BaseTest {
	
	@Autowired
	private OutRecordService outRecordService;
	
	@Test
	public void testlistOutRecrds() {
		
		List<OutRecord> outRecords = null;
		try {
//			outRecords = outRecordService.listOutRecrds();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(outRecords.size());
		
		System.out.println(outRecords.toString());
	}
	
	
}
