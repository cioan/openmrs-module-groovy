/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.groovy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
public class GroovyTaskTest extends BaseModuleContextSensitiveTest  {
	@Autowired
	GroovyTask task;
	
	@Before
    public void before() throws Exception {
        //executeDataSet("EncounterServiceTest-initialData.xml");
        executeDataSet("GroovyTaskTest-initialData.xml");
    }
	
	@Test
	public void execute() {
		TaskDefinition def = new TaskDefinition();
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("script name", "hello world");
		def.setProperties(properties );
		task.initialize(def);
		task.execute();
	}
	
	public void testEncounterService() {
		EncounterService es = Context.getEncounterService();
        
        // First, create a new Encounter
        Encounter enc = new Encounter();
        enc.setLocation(new Location(1));
        enc.setEncounterType(new EncounterType(1));
        enc.setEncounterDatetime(new Date());
        enc.setPatient(new Patient(3));
        // this is 1.9.1
        //enc.setProvider(new EncounterRole(1), new Provider(1));
        es.saveEncounter(enc);
        
        // Now add an obs to it
        Obs newObs = new Obs();
        newObs.setConcept(new Concept(1));
        newObs.setValueNumeric(50d);
        
        enc.addObs(newObs);
        es.saveEncounter(enc);
        
        assert(null != newObs.getObsId());

	}
}