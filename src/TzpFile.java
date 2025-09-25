import java.nio.ByteBuffer;

/**
 * Created by Noxid on 24-Sept-24.
 */
public class TzpFile extends TmdFile {
	int poseCount;

	TzpFile(ByteBuffer data) {
		super();
		headerSize = 24;
		poseCount = data.getInt();
		data.getInt(); // unk01
		data.getInt(); // unk02
		readHeader(data);
		readModels(data);

		// pose data - lists of replacement vertex positions
		// and normals
		Model basis = objList.get(0);
		int vertBegin = headerSize + basis.filesize;
		int normalBegin = vertBegin + poseCount * basis.nVert * 4;
		System.out.printf("Creating %d poses\n", poseCount);
		for (int i = 0; i < poseCount; i++) {
			TzpPose pose = new TzpPose(basis, vertBegin, normalBegin, i);
			pose.populate(data);
			objList.add(pose.makePosedModel());
		}
	}
}
