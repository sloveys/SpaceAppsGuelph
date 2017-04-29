/* JAI-Ext - OpenSource Java Advanced Image Extensions Library
 *    http://www.geo-solutions.it/
 *    Copyright 2014 GeoSolutions


 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.geosolutions.jaiext.utilities.shape;

import java.awt.geom.PathIterator;

/**
 * Subclass that provides a convenient efficient currentSegment(float[] coords) implementation that reuses always the same double array. This class
 * and the associated subclasses are not thread safe. This class was ported back and simplified from GeoTools, with permission from the author(s)
 * 
 * @author Andrea Aime
 */
public abstract class AbstractLiteIterator implements PathIterator {
    /** Segment coordinates*/
    protected double[] dcoords = new double[2];

    /**
     * @see java.awt.geom.PathIterator#currentSegment(float[])
     */
    public int currentSegment(float[] coords) {
        int result = currentSegment(dcoords);
        coords[0] = (float) dcoords[0];
        coords[1] = (float) dcoords[1];

        return result;
    }

}
