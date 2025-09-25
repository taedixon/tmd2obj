import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by Noxid on 24-Sept-24.
 */
public class TzpPose {
	private ArrayList<Vec3> vertexDiffs = new ArrayList<>();
	private ArrayList<Vec3> normalDiffs = new ArrayList<>();

	Model basis;
	int poseIndex;
	int vertexAddress;
	int normalAddress;

	TzpPose(Model basis, int vertexBase, int normalBase, int poseId) {
		this.basis = basis;
		poseIndex = poseId;
		vertexAddress = vertexBase + (poseIndex * basis.nVert * 4);
		normalAddress = normalBase + (poseIndex * basis.nNorm * 4);
	}

	void populate(ByteBuffer data) {
		System.out.printf("Begin vertex pose data 0x%X\n", vertexAddress);
		data.position(vertexAddress);
		for (int i = 0; i < basis.nVert; i++) {
			Vec3 vd = new Vec3(data.get(), data.get(), data.getShort());
			vertexDiffs.add(vd);
		}
		System.out.printf("Begin normal pose data 0x%X\n", normalAddress);
		data.position(normalAddress);
		for (int i = 0; i < basis.nNorm; i++) {
			normalDiffs.add(new Vec3(data.get(), data.get(), data.get()));
			data.get(); // pad;
		}
	}

	public Model makePosedModel() {
		return new Model(basis, vertexDiffs, normalDiffs);
	}
}
