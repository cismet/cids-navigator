/*
 * Copyright (C) 2015 cismet GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.cismet.cids.stepprotocol.impl;

import de.cismet.cids.stepprotocol.AbstractStepProtocol;
import de.cismet.cids.stepprotocol.AbstractStepProtocolPanel;
import de.cismet.cids.stepprotocol.StepProtocolMetaInfo;

/**
 *
 * @author jruiz
 */
public class EmptyStepProtocol extends AbstractStepProtocol {

    @Override
    protected StepProtocolMetaInfo createMetaInfo() {
        return null;
    }

    @Override
    public AbstractStepProtocolPanel visualize() {
        return null;
    }
    
}
