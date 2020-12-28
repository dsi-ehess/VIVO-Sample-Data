package exemple;

import static edu.cornell.mannlib.vitro.webapp.modelaccess.ModelNames.TBOX_ASSERTIONS;
import static edu.cornell.mannlib.vitro.webapp.modelaccess.ModelNames.TBOX_INFERENCES;
import static edu.cornell.mannlib.vitro.webapp.modelaccess.ModelNames.TBOX_UNION;

import edu.cornell.mannlib.vitro.webapp.tboxreasoner.ReasonerConfiguration;
import edu.cornell.mannlib.vitro.webapp.tboxreasoner.impl.BasicTBoxReasonerDriver;
import edu.cornell.mannlib.vitro.webapp.tboxreasoner.impl.jfact.JFactTBoxReasoner;

public class TestVitroReasonner {

	public static void main(String[] args) {
		JFactTBoxReasoner rea = new JFactTBoxReasoner();
		BasicTBoxReasonerDriver driver;
		driver = new BasicTBoxReasonerDriver(
				contextModels.getOntModel(TBOX_ASSERTIONS), contextModels
						.getOntModel(TBOX_INFERENCES).getBaseModel(),
				contextModels.getOntModel(TBOX_UNION), reasoner,
				ReasonerConfiguration.DEFAULT);
	}

}
