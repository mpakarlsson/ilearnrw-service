package com.ilearnrw.api.selectnextactivity;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ilearnrw.user.profile.UserProfile;
import com.ilearnrw.api.datalogger.services.CubeService;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.app.games.mapping.GamesInformation;
import com.ilearnrw.common.security.users.services.UserService;

@Controller
public class SelectNextActivityController {

	private static Logger LOG = Logger
			.getLogger(SelectNextActivityController.class);

	@Autowired
	IProfileProvider profileProvider;

	@Autowired
	UserService userService;

	@Autowired
	CubeService cubeService;


	@RequestMapping(value = "/activity/next", method = RequestMethod.GET)
	public @ResponseBody
	List<NextActivities> selectNextActivity(
			@RequestParam(value = "userId", required = true) int userId) {
		
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		String username = auth.getName();
		LOG.debug(String.format("Retrieving next activity for user %s", username));

		UserProfile user = null;
		try {
			user = profileProvider.getProfile(userId);
		} catch (ProfileProviderException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ArrayList<String> appIds = new ArrayList<String>();
			appIds.add("MAIL_SORTER");
			appIds.add("WHAK_A_MOLE");
			appIds.add("ENDLESS_RUNNER");
			appIds.add("HARVEST");
			appIds.add("SERENADE_HERO");
			appIds.add("MOVING_PATHWAYS");
			appIds.add("EYE_EXAM");
			appIds.add("TRAIN_DISPATCHER");
			appIds.add("DROP_CHOPS");

			//NextActivitiesProvider provider = new WorkingIndexBasedAlgorithm();
			NextActivitiesProvider provider = new ClusterBasedAlgorithm();
					
			List<NextActivities> next = provider.getNextProblems(user);
			for (NextActivities n : next){
				ArrayList<String> apps = GamesInformation.getProblemRelatedAppsString(n.getCategory(), user.getLanguage());
				System.err.println(apps.toString());
				n.setActivity(apps);
			}
			return next;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
