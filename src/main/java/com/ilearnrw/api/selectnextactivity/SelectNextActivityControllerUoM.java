package com.ilearnrw.api.selectnextactivity;

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
import com.ilearnrw.common.security.users.services.UserService;

@Controller
public class SelectNextActivityControllerUoM {

	private static Logger LOG = Logger
			.getLogger(SelectNextActivityControllerUoM.class);

	@Autowired
	IProfileProvider profileProvider;

	@Autowired
	UserService userService;

	@Autowired
	CubeService cubeService;


	@RequestMapping(value = "/activity/next_UoM", method = RequestMethod.GET)
	public @ResponseBody
	List<NextActivities> selectNextActivity(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "languageArea",required = false) Integer languageArea,
			@RequestParam(value = "difficulty",required = false) Integer difficulty,
			@RequestParam(value = "game",required = false) String game
			) {
		
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

			//NextActivitiesProvider provider = new WorkingIndexBasedAlgorithm();
			DecisionTreeBasedAlgorithm provider = new DecisionTreeBasedAlgorithm();
			
			if((languageArea!=null)&&(difficulty!=null)&&(game!=null)){
				return provider.getNextProblems(user,languageArea, difficulty,game);

			}else if((languageArea!=null)&&(difficulty!=null)){
				return provider.getNextProblems(user,languageArea, difficulty);
			}else{
				return provider.getNextProblems(user);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
