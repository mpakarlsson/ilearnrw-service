package com.ilearnrw.api.selectnextactivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ilearnrw.user.profile.UserProfile;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ilearnrw.api.datalogger.services.CubeService;
import com.ilearnrw.api.info.dao.InfoDaoImpl;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.app.games.mapping.GamesInformation;
import com.ilearnrw.common.security.users.model.User;
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
			appIds.add("HARVEST");
			appIds.add("SERENADE_HERO");
			appIds.add("MOVING_PATHWAYS");
			appIds.add("EYE_EXAM");
			appIds.add("TRAIN_DISPATCHER");
			appIds.add("DROP_CHOPS");
			
			List<NextActivities> dummy = new ArrayList<NextActivities>();
			
			Random rand = new Random();
			String first = appIds.remove(rand.nextInt(appIds.size()));
			String second = appIds.remove(rand.nextInt(appIds.size()));

			NextActivities dummy1 = new NextActivities();

			System.err.println(first);
			dummy1.addActivity(first);
			ArrayList<Integer> cats = GamesInformation.getAppRelatedProblems(first, user.getLanguage());
			System.err.println(cats.toString());
			int cat = rand.nextInt(cats.size());
			int len = user.getUserProblems().getRowLength(cat);
			int idx = rand.nextInt(len);
			dummy1.setProblem(cat, idx);
			dummy.add(dummy1);
			dummy1 = new NextActivities();
			
			dummy1.addActivity(second);
			cats = GamesInformation.getAppRelatedProblems(second, user.getLanguage());
			cat = rand.nextInt(cats.size());
			len = user.getUserProblems().getRowLength(cat);
			idx = rand.nextInt(len);
			dummy1.setProblem(cat, idx);
			dummy.add(dummy1);
			return dummy;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
