/*
 * Encog(tm) Workbench v2.6 
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2010 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package org.encog.workbench.process;

import org.encog.bot.BotUtil;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.MLMethod;
import org.encog.ml.svm.KernelType;
import org.encog.ml.svm.SVMType;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.ADALINEPattern;
import org.encog.neural.pattern.ART1Pattern;
import org.encog.neural.pattern.BAMPattern;
import org.encog.neural.pattern.BoltzmannPattern;
import org.encog.neural.pattern.CPNPattern;
import org.encog.neural.pattern.ElmanPattern;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.neural.pattern.HopfieldPattern;
import org.encog.neural.pattern.JordanPattern;
import org.encog.neural.pattern.NEATPattern;
import org.encog.neural.pattern.RadialBasisPattern;
import org.encog.neural.pattern.SOMPattern;
import org.encog.neural.pattern.SVMPattern;
import org.encog.persist.EncogMemoryCollection;
import org.encog.persist.EncogPersistedObject;
import org.encog.workbench.EncogWorkBench;
import org.encog.workbench.dialogs.createnetwork.CreateADALINEDialog;
import org.encog.workbench.dialogs.createnetwork.CreateART1;
import org.encog.workbench.dialogs.createnetwork.CreateAutomatic;
import org.encog.workbench.dialogs.createnetwork.CreateBAMDialog;
import org.encog.workbench.dialogs.createnetwork.CreateBlotzmannDialog;
import org.encog.workbench.dialogs.createnetwork.CreateCPNDialog;
import org.encog.workbench.dialogs.createnetwork.CreateElmanDialog;
import org.encog.workbench.dialogs.createnetwork.CreateFeedforward;
import org.encog.workbench.dialogs.createnetwork.CreateHopfieldDialog;
import org.encog.workbench.dialogs.createnetwork.CreateJordanDialog;
import org.encog.workbench.dialogs.createnetwork.CreateNEATDialog;
import org.encog.workbench.dialogs.createnetwork.CreateNeuralNetworkDialog;
import org.encog.workbench.dialogs.createnetwork.CreateRBFDialog;
import org.encog.workbench.dialogs.createnetwork.CreateRSOMDialog;
import org.encog.workbench.dialogs.createnetwork.CreateSOMDialog;
import org.encog.workbench.dialogs.createnetwork.CreateSVMDialog;
import org.encog.workbench.dialogs.createnetwork.NeuralNetworkType;
import org.encog.workbench.frames.document.tree.ProjectEGFile;
import org.encog.workbench.tabs.incremental.IncrementalPruneTab;

public class CreateNeuralNetwork {

	public static void process(String name, ProjectEGFile pef) {
		EncogPersistedObject network = null;
		CreateNeuralNetworkDialog dialog = new CreateNeuralNetworkDialog(
				EncogWorkBench.getInstance().getMainWindow());
		dialog.setType(NeuralNetworkType.Feedforward);
		if (dialog.process()) {
			switch (dialog.getType()) {
			case Automatic:
				EncogMemoryCollection encog = pef.getCollection();
				createAutomatic(encog,name);
				network=null;
				break;
			case Feedforward:
				network = createFeedForward(name);
				break;
			case SOM:
				network = createSOM(name);
				break;
			case Hopfield:
				network = createHopfield(name);
				break;
			case Elman:
				network = createElman(name);
				break;
			case Jordan:
				network = createJordan(name);
				break;
			case RBF:
				network = createRBF(name);
				break;
			case BAM:
				network = createBAM(name);
				break;
			case CPN:
				network = createCPN(name);
				break;
			case Boltzmann:
				network = createBoltzmann(name);
				break;
			case ADALINE:
				network = createADALINE(name);
				break;
			case ART1:
				network = createART1(name);
				break;		
			case NEAT:
				network = createNEAT(name);
				break;
			case SVM:
				network = createSVM(name);
				break;
			default:
				network = createEmpty(name);
				break;
			}

			if (network != null) {
				EncogMemoryCollection encog = pef.getCollection();
				encog.add(name, network);
				encog.save(pef.getFile().toString());
				pef.generateChildrenList();
				EncogWorkBench.getInstance().getMainWindow().redraw();
			}
		}
	}

	private static EncogPersistedObject createSVM(String name) {
		CreateSVMDialog dialog = new CreateSVMDialog(EncogWorkBench
				.getInstance().getMainWindow());
		dialog.setSVMType(SVMType.EpsilonSupportVectorRegression);
		dialog.setKernelType(KernelType.RadialBasisFunction);
		if (dialog.process()) {
			SVMPattern svm = new SVMPattern();
			svm.setInputNeurons(dialog.getInputCount().getValue());
			svm.setOutputNeurons(dialog.getOutputCount().getValue());
			svm.setKernelType(dialog.getKernelType());
			svm.setSVMType(dialog.getSVMType());
			return svm.generate();
		} else
			return null;
	}

	
	private static EncogPersistedObject createRBF(String name) {
		CreateRBFDialog dialog = new CreateRBFDialog(EncogWorkBench
				.getInstance().getMainWindow());
		if (dialog.process()) {
			RadialBasisPattern rbf = new RadialBasisPattern();
			rbf.setInputNeurons(dialog.getInputCount().getValue());
			rbf.addHiddenLayer(dialog.getHiddenCount().getValue());
			rbf.setOutputNeurons(dialog.getOutputCount().getValue());
			return rbf.generate();
		} else
			return null;

	}

	private static EncogPersistedObject createJordan(String name) {
		CreateJordanDialog dialog = new CreateJordanDialog(EncogWorkBench
				.getInstance().getMainWindow());
		if (dialog.process()) {
			JordanPattern jordan = new JordanPattern();
			jordan.setInputNeurons(dialog.getInputCount().getValue());
			jordan.addHiddenLayer(dialog.getHiddenCount().getValue());
			jordan.setOutputNeurons(dialog.getOutputCount().getValue());
			jordan.setActivationFunction(new ActivationTANH());
			return jordan.generate();
		} else
			return null;

	}

	private static EncogPersistedObject createElman(String name) {
		CreateElmanDialog dialog = new CreateElmanDialog(EncogWorkBench
				.getInstance().getMainWindow());
		if (dialog.process()) {
			ElmanPattern elman = new ElmanPattern();
			elman.setInputNeurons(dialog.getInputCount().getValue());
			elman.addHiddenLayer(dialog.getHiddenCount().getValue());
			elman.setOutputNeurons(dialog.getOutputCount().getValue());
			elman.setActivationFunction(new ActivationTANH());
			return elman.generate();
		} else
			return null;

	}

	private static EncogPersistedObject createHopfield(String name) {
		CreateHopfieldDialog dialog = new CreateHopfieldDialog(EncogWorkBench
				.getInstance().getMainWindow());
		if (dialog.process()) {
			HopfieldPattern hopfield = new HopfieldPattern();
			hopfield.setInputNeurons(dialog.getNeuronCount().getValue());
			return hopfield.generate();
		} else
			return null;
	}

	private static EncogPersistedObject createSOM(String name) {
		CreateSOMDialog dialog = new CreateSOMDialog(EncogWorkBench
				.getInstance().getMainWindow());
		if (dialog.process()) {
			SOMPattern som = new SOMPattern();
			som.setInputNeurons(dialog.getInputCount().getValue());
			som.setOutputNeurons(dialog.getOutputCount().getValue());
			return som.generate();
		} else
			return null;
	}

	private static EncogPersistedObject createFeedForward(String name) {
		CreateFeedforward dialog = new CreateFeedforward(EncogWorkBench
				.getInstance().getMainWindow());
		dialog.setActivationFunctionHidden(new ActivationTANH());
		dialog.setActivationFunctionOutput(new ActivationTANH());
		
		if (dialog.process()) {
			FeedForwardPattern feedforward = new FeedForwardPattern();
			feedforward.setActivationFunction(dialog.getActivationFunctionHidden());
			feedforward.setActivationOutput(dialog.getActivationFunctionOutput());
			feedforward.setInputNeurons(dialog.getInputCount().getValue());
			for (int i = 0; i < dialog.getHidden().getModel().size(); i++) {
				String str = (String) dialog.getHidden().getModel()
						.getElementAt(i);
				int i1 = str.indexOf(':');
				int i2 = str.indexOf("neur");
				if (i1 != -1 && i2 != -1) {
					str = str.substring(i1 + 1, i2).trim();
					int neuronCount = Integer.parseInt(str);
					feedforward.addHiddenLayer(neuronCount);
				}
			}
			feedforward.setInputNeurons(dialog.getInputCount().getValue());
			feedforward.setOutputNeurons(dialog.getOutputCount().getValue());
			return feedforward.generate();
		}
		return null;

	}

	private static BasicNetwork createAutomatic(EncogMemoryCollection collection, String name) {
		CreateAutomatic dialog = new CreateAutomatic(EncogWorkBench
				.getInstance().getMainWindow());
		dialog.setActivationFunction(new ActivationTANH());
		
		dialog.getWeightTries().setValue(5);
		dialog.getIterations().setValue(25);
		dialog.getWindowSize().setValue(10);

		if (dialog.process()) {
			NeuralDataSet training = dialog.getTraining();			
			
			if( training == null ) {
				return null;
			}
			
			
			FeedForwardPattern pattern = new FeedForwardPattern();
			pattern.setInputNeurons(training.getInputSize());
			pattern.setOutputNeurons(training.getIdealSize());
		
			
			pattern.setActivationFunction(dialog.getActivationFunction());			
			IncrementalPruneTab tab = new IncrementalPruneTab(
					dialog.getIterations().getValue(),
					dialog.getWeightTries().getValue(),
					dialog.getWindowSize().getValue(),
					training,
					pattern,
					name,
					collection);
			
			for (int i = 0; i < dialog.getHidden().getModel().size(); i++) {
				String str = (String) dialog.getHidden().getModel()
						.getElementAt(i);
				
				String lowStr = BotUtil.extract(str,"low=",".",0);
				String highStr = BotUtil.extract(str,"high=",",",0);
				int low = Integer.parseInt(lowStr);
				int high = Integer.parseInt(highStr);
				
				tab.addHiddenRange(low,high);
			}
			
			

			EncogWorkBench.getInstance().getMainWindow().openModalTab(tab, "Incremental Prune");
			return null;
		}
		return null;

	}
	
	private static BasicNetwork createEmpty(String name) {
		BasicNetwork network = new BasicNetwork();
		network.setName(name);
		return network;
	}
	
	private static EncogPersistedObject createADALINE(String name) {
		CreateADALINEDialog dialog = new CreateADALINEDialog(EncogWorkBench
				.getInstance().getMainWindow());
		if (dialog.process()) {
			ADALINEPattern adaline = new ADALINEPattern();
			adaline.setInputNeurons(dialog.getNeuronCount().getValue());
			adaline.setOutputNeurons(dialog.getElementCount().getValue());
			return adaline.generate();
		} else
			return null;
	}
	
	private static EncogPersistedObject createBAM(String name) {
		CreateBAMDialog dialog = new CreateBAMDialog(EncogWorkBench
				.getInstance().getMainWindow());
		if (dialog.process()) {
			BAMPattern bam = new BAMPattern();
			bam.setF1Neurons(dialog.getLayerACount().getValue());
			bam.setF2Neurons(dialog.getLayerBCount().getValue());
			return bam.generate();
		} else
			return null;
	}
	
	private static EncogPersistedObject createBoltzmann(String name) {
		CreateBlotzmannDialog dialog = new CreateBlotzmannDialog(EncogWorkBench
				.getInstance().getMainWindow());
		if (dialog.process()) {
			BoltzmannPattern boltz = new BoltzmannPattern();
			boltz.setInputNeurons(dialog.getNeuronCount().getValue());
			return boltz.generate();
		} else
			return null;
	}
	
	private static EncogPersistedObject createCPN(String name) {
		CreateCPNDialog dialog = new CreateCPNDialog(EncogWorkBench
				.getInstance().getMainWindow());
		if (dialog.process()) {
			CPNPattern cpn = new CPNPattern();
			cpn.setInputNeurons(dialog.getInputCount().getValue());
			cpn.setInstarCount(dialog.getInstarCount().getValue());
			cpn.setOutstarCount(dialog.getOutstarCount().getValue());
			return cpn.generate();
		} else
			return null;
	}
	
	private static EncogPersistedObject createART1(String name) {
		CreateART1 dialog = new CreateART1(EncogWorkBench
				.getInstance().getMainWindow());
		if (dialog.process()) {
			ART1Pattern art1 = new ART1Pattern();
			art1.setInputNeurons(dialog.getF1().getValue());
			art1.setOutputNeurons(dialog.getF2().getValue());
			return art1.generate();
		} else
			return null;
	}
	
	private static EncogPersistedObject createNEAT(String name) {
		CreateNEATDialog dialog = new CreateNEATDialog(EncogWorkBench
				.getInstance().getMainWindow());
		dialog.setOutputActivationFunction(new ActivationSigmoid());
		dialog.setNeatActivationFunction(new ActivationSigmoid());
		if (dialog.process()) {
			NEATPattern feedforward = new NEATPattern();
			feedforward.setActivationFunction(dialog.getOutputActivationFunction());
			feedforward.setNEATActivationFunction(dialog.getNeatActivationFunction());
			feedforward.setInputNeurons(dialog.getInputCount().getValue());
			feedforward.setInputNeurons(dialog.getInputCount().getValue());
			feedforward.setOutputNeurons(dialog.getOutputCount().getValue());
			return feedforward.generate();
		}
		return null;

	}
	
}
