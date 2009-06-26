/*
 * Encog Workbench v2.x
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2009, Heaton Research Inc., and individual contributors.
 * See the copyright.txt in the distribution for a full listing of 
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.encog.workbench.dialogs.createnetwork;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.encog.workbench.dialogs.common.EncogCommonDialog;
import org.encog.workbench.dialogs.common.ValidationException;

public class CreateNeuralNetworkDialog extends EncogCommonDialog implements ListSelectionListener {

	private DefaultListModel model = new DefaultListModel();
	private JList list = new JList(model);
	private JTextArea text = new JTextArea();
	private JScrollPane scroll1 = new JScrollPane(list);
	private JScrollPane scroll2 = new JScrollPane(text);
	private NeuralNetworkType type;
	
	public CreateNeuralNetworkDialog(Frame owner) {
		super(owner);
		setTitle("Create a Neural Network");

		this.setSize(500, 250);
		this.setLocation(50, 100);
		final Container content = getBodyPanel();
		content.setLayout(new GridLayout(1,2));
		
		content.add(this.scroll1);
		content.add(this.scroll2);
		
		this.model.addElement("Empty Neural Network");
		this.model.addElement("Feedforward Neural Network");
		this.model.addElement("Self Organizing Map (SOM)");
		this.model.addElement("Hopfield Neural Network");
		this.model.addElement("Recurrent - Elman");
		this.model.addElement("Recurrent - Jordan");
		this.model.addElement("Recurrent - SOM");
		this.model.addElement("Feedforward - Radial Basis");
		this.list.addListSelectionListener(this);
		this.text.setLineWrap(true);
		this.text.setWrapStyleWord(true);
		this.text.setEditable(false);
		scroll2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5882600361686632769L;

	@Override
	public void collectFields() throws ValidationException {
		switch(list.getSelectedIndex())
		{
		case 0:
			this.type = NeuralNetworkType.Empty;
			break;
		case 1:
			this.type = NeuralNetworkType.Feedforward;
			break;
		case 2:
			this.type = NeuralNetworkType.SOM;
			break;
		case 3:
			this.type = NeuralNetworkType.Hopfield;
			break;
		case 4:
			this.type = NeuralNetworkType.Elman;
			break;
		case 5:
			this.type = NeuralNetworkType.Jordan;
			break;
		case 6:
			this.type = NeuralNetworkType.RSOM;
			break;
		case 7:
			this.type = NeuralNetworkType.RBF;
			break;
		}
		
	}

	@Override
	public void setFields() {
		switch(type)
		{
		case Empty:
			this.list.setSelectedIndex(0);
			break;
		case Feedforward:
			this.list.setSelectedIndex(1);
			break;
		case SOM:
			this.list.setSelectedIndex(2);
			break;
		case Hopfield:
			this.list.setSelectedIndex(3);
			break;
		case Elman:
			this.list.setSelectedIndex(4);
			break;
		case Jordan:
			this.list.setSelectedIndex(5);
			break;
		case RBF:
			this.list.setSelectedIndex(6);
			break;
		}
		
	}

	public NeuralNetworkType getType() {
		return type;
	}

	public void setType(NeuralNetworkType type) {
		this.type = type;
	}

	public void valueChanged(ListSelectionEvent e) {
		switch(list.getSelectedIndex())
		{
		case 0:
			this.text.setText("Empty Neural Network - Creates a blank neural network that you can add layers and synapses to.  This allows you to create a neural network from scratch.");
			break;
		case 1:
			this.text.setText("Feed Forward Neural Network - A simple neural network type where synapses are made from an input layer to zero or more hidden layers, and finally to an output layer.  The feedforward neural network is one of the most common types of neural network in use.  It is suitable for many types of problems.  Feedforward neural networks are often trained with simulated annealing, genetic algorithms or one of the propagation techniques.");
			break;
		case 2:
			this.text.setText("Self Organizing Map (SOM) - A neural network that contains two layers and implements a winner take all strategy in the output layer.  Rather than taking the output of individual neurons, the neuron with the highest output is considered the winner.  SOM's are typically used for classification, where the output neurons represent groups that the input neurons are to be classified into.  SOM's are usually trained with a competitive learning strategy.");
			break;
		case 3:
			this.text.setText("Hopfield Neural Network - A simple single layer recurrent neural network.  The Hopfield neural network is trained with a special algorithm that teaches it to learn to recognize patterns.  The Hopfield network will indicate that the pattern is recognized by echoing it back.  Hopfield neural networks are typically used for pattern recognition.");
			break;
		case 4:
			this.text.setText("Simple Recurrent Network (SRN) Elman Style - A recurrent neural network that has a context layer.  The context layer holds the previous output from the hidden layer and then echos that value back to the hidden layer's input.  The hidden layer then always receives input from its previous iteration's output.  Elman neural networks are generally trained using genetic, simulated annealing, or one of the propagation techniques.  Elman neural networks are typically used for prediction.");
			break;
		case 5:
			this.text.setText("Simple Recurrent Network (SRN) Jordan Style - A recurrent neural network that has a context layer.  The context layer holds the previous output from the output layer and then echos that value back to the hidden layer's input.  The hidden layer then always receives input from the previous iteration's output layer.  Jordan neural networks are generally trained using genetic, simulated annealing, or one of the propagation techniques.  Jordan neural networks are typically used for prediction.");
			break;
		case 6:
			this.text.setText("Simple Recurrent Network (SRN) Self Organizing Map - A recurrent self organizing map that has an input and output layer, just as a regular SOM.  However, the RSOM has a context layer as well.  This context layer echo's the previous iteration's output back to the input layer of the neural network.  RSOM's are trained with a competitive learning algorithm, just as a non-recurrent SOM.  RSOM's can be used to classify temporal data, or to predict.");
			break;
		case 7:
			this.text.setText("Feedforward Radial Basis Function (RBF) Network - A feedforward network with an input layer, output layer and a hidden layer.  The hidden layer is based on a radial basis function.  The RBF generally used is the gaussian function.  Several RBF's in the hidden layer allow the RBF network to approximate a more complex activation function than a typical feedforward neural network.  RBF networks are used for pattern recognition.  They can be trained using genetic, annealing or one of the propagation techniques.  Other means must be employed to determine the structure of the RBF's used in the hidden layer.");
			break;
		}
		
		this.text.setSelectionStart(0);
		this.text.setSelectionEnd(0);
		
	}
	
	

}
