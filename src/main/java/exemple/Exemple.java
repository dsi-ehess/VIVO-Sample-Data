package exemple;
import java.io.FileOutputStream;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.apibinding.OWLManager;

import uk.ac.manchester.cs.jfact.JFactFactory;

public class Exemple {

	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
        JFactFactory reasonerFactory = new JFactFactory();
        OWLOntology pizzaOntology = OWLManager.createOWLOntologyManager().loadOntology(
                IRI.create("file:///home/heon/00-PROJECT-VIVO-EHESS/00-GIT/VIVO-Sample-Data/restriction-dev/test-case-restriction.ttl"));
        // a config object. Things like monitor, timeout, etc, go here
        OWLReasonerConfiguration config = new SimpleConfiguration(50000);
        // Create a reasoner that will reason over our ontology and its imports
        // closure. Pass in the configuration.
        OWLReasoner reasoner = reasonerFactory.createReasoner(pizzaOntology, config);
        // Ask the reasoner to classify the ontology
        reasoner.precomputeInferences(InferenceType.values());
        // We can determine if the ontology is actually consistent (in this
        // case, it should be).
        System.err.println(reasoner.isConsistent());
        // get a list of unsatisfiable classes
        Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
        // leave owl:Nothing out
        Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
        if (!unsatisfiable.isEmpty()) {
            System.out.println("The following classes are unsatisfiable: ");
            for (OWLClass cls : unsatisfiable) {
                System.out.println(cls.getIRI().getFragment());
            }
        } else {
            System.out.println("There are no unsatisfiable classes");
        }
        // Look up and print all direct subclasses for all classes
        for (OWLClass c : pizzaOntology.getClassesInSignature()) {
            // the boolean argument specifies direct subclasses; false would
            // specify all subclasses
            // a NodeSet represents a set of Nodes.
            // a Node represents a set of equivalent classes
            NodeSet<OWLClass> subClasses = reasoner.getSubClasses(c, true);
            for (OWLClass subClass : subClasses.getFlattened()) {
                System.out.println(subClass.getIRI().getFragment() + "\tsubclass of\t"
                        + c.getIRI().getFragment());
            }
        }
        // for each class, look up the instances
        for (OWLClass c : pizzaOntology.getClassesInSignature()) {
            // the boolean argument specifies direct subclasses; false would
            // specify all subclasses
            // a NodeSet represents a set of Nodes.
            // a Node represents a set of equivalent classes/or sameAs
            // individuals
            NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, true);
            for (OWLNamedIndividual i : instances.getFlattened()) {
//            	if (!i.getIRI().getFragment().contains("prs_0000002_indva")) break; 
                System.out.println(i.getIRI().getFragment() + "\tinstance of\t"
                        + c.getIRI().getFragment());
                // look up all property assertions
                for (OWLObjectProperty op : pizzaOntology
                        .getObjectPropertiesInSignature()) {
                    NodeSet<OWLNamedIndividual> petValuesNodeSet = reasoner
                            .getObjectPropertyValues(i, op);
                    for (OWLNamedIndividual value : petValuesNodeSet.getFlattened()) {
                        System.out.println(i.getIRI().getFragment() + "\t"
                                + op.getIRI().getFragment() + "\t"
                                + value.getIRI().getFragment());
                    }
                }
            }
        }
//        OWLOntology ronto = reasoner.getRootOntology();
//        pizzaOntology.getOWLOntologyManager().saveOntology(ronto, new TurtleDocumentFormat(), new StreamDocumentTarget(System.out));
    }

}
