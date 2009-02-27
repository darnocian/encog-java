/*
 * Encog Artificial Intelligence Framework v1.x
 * Java Version
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
package org.encog.neural.persist.persistors;

import javax.xml.transform.sax.TransformerHandler;

import org.encog.matrix.Matrix;
import org.encog.neural.NeuralNetworkError;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.persist.EncogPersistedCollection;
import org.encog.neural.persist.EncogPersistedObject;
import org.encog.neural.persist.Persistor;
import org.encog.util.XMLUtil;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Persistence methods for the basic neural layer.
 * 
 * @author jheaton
 */
public class BasicLayerPersistor implements Persistor {

	/**
	 * Load from the specified node.
	 * 
	 * @param layerNode
	 *            The node to load from.
	 * @return The EncogPersistedObject that was loaded.
	 */
	public EncogPersistedObject load(final Element layerNode) {
		final String str = layerNode.getAttribute("neuronCount");
		final int neuronCount = Integer.parseInt(str);

		final String name = layerNode.getAttribute("name");
		final String description = layerNode.getAttribute("description");

		final BasicLayer layer = new BasicLayer(neuronCount);
		layer.setName(name);
		layer.setDescription(description);
		final Element matrixElement = XMLUtil.findElement(layerNode,
				"weightMatrix");
		if (matrixElement != null) {
			final Element e = XMLUtil.findElement(matrixElement, "Matrix");
			final Persistor persistor = EncogPersistedCollection
					.createPersistor("Matrix");
			final Matrix matrix = (Matrix) persistor.load(e);
			layer.getNext().setMatrix(matrix);
		}
		return layer;
	}

	/**
	 * Save the specified object.
	 * 
	 * @param object
	 *            The object to save.
	 * @param hd
	 *            The XML object.
	 */
	public void save(final EncogPersistedObject object,
			final TransformerHandler hd) {

		try {
			final BasicLayer layer = (BasicLayer) object;

			final AttributesImpl atts = EncogPersistedCollection
					.createAttributes(object);
			EncogPersistedCollection.addAttribute(atts, "neuronCount", ""
					+ layer.getNeuronCount());

			hd.startElement("", "", layer.getClass().getSimpleName(), atts);

			atts.clear();

			if (layer.getNext()!=null) {

				final Persistor persistor = EncogPersistedCollection
						.createPersistor(layer.getNext().getMatrix().getClass()
								.getSimpleName());
				atts.clear();
				hd.startElement("", "", "weightMatrix", atts);
				persistor.save(layer.getNext().getMatrix(), hd);
				hd.endElement("", "", "weightMatrix");

			}

			hd.endElement("", "", layer.getClass().getSimpleName());
		} catch (final SAXException e) {
			throw new NeuralNetworkError(e);
		}
	}

}
