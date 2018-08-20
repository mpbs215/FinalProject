package kosta.mvc.model.user.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kosta.mvc.model.dao.AuthoritiesDAO;
import kosta.mvc.model.dao.CarTypeDAO;
import kosta.mvc.model.dao.ParkDAO;
import kosta.mvc.model.dao.ParkImgDAO;
import kosta.mvc.model.dao.ParkReserveDAO;
import kosta.mvc.model.dao.RegiDAO;
import kosta.mvc.model.dao.ReviewDAO;
import kosta.mvc.model.dao.SidogugundongriDAO;
import kosta.mvc.model.dao.UserDAO;
import kosta.mvc.model.dto.AuthorityDTO;
import kosta.mvc.model.dto.CarTypeDTO;
import kosta.mvc.model.dto.ParkDTO;
import kosta.mvc.model.dto.ParkImgDTO;
import kosta.mvc.model.dto.ParkRegiDTO;
import kosta.mvc.model.dto.ParkReserveDTO;
import kosta.mvc.model.dto.ReviewDTO;
import kosta.mvc.model.dto.TempKeyDTO;
import kosta.mvc.model.dto.SidogugundongriDTO;
import kosta.mvc.model.dto.UserDTO;

@Service
@Transactional
public class UserServiceImpl {

	@Autowired
	private ReviewDAO reviewDAO;
	@Autowired
	private ParkDAO parkDAO;
	@Autowired
	private ParkImgDAO parkImgDAO;
	@Autowired
	private RegiDAO regiDAO;
	@Autowired
	private ParkReserveDAO parkReserveDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private CarTypeDAO carTypeDAO;
	@Autowired
	private AuthoritiesDAO authoritiesDAO;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 *	회원가입하기
	 * */
	@Transactional
	public void signUp(UserDTO userDTO) {

		// 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
		userDTO.setPassword(encodedPassword);
		userDAO.signUp(userDTO);
		
		AuthorityDTO authDTO = new AuthorityDTO();
		authDTO.setUserId(userDTO.getUserId());
		authDTO.setRole("ROLE_USER");
		authDTO.setKey("0");
		authDTO.setHp(userDTO.getHp());
		
		System.out.println("Auth테이블의 아이디는 : " + authDTO.getUserId());
		
		if (userDTO.getSeller() == 0 ) {
			int result = authoritiesDAO.insertAuthority(authDTO);
			System.out.println("권한 등록하려는데 : " +result);
			
			if (result ==0) {
				throw new RuntimeException("권한 등록에 실패 하였습니다. / 회원가입을 진행해 주세요");
			}
		}  
	}

	public List<ReviewDTO> userClickReviewStar(int parkNo, int rating) {
		return reviewDAO.userClickReviewStar(parkNo, rating);
	}

	public ParkDTO selectOnePark(int parkNo) {
		return parkDAO.selectOnePark(parkNo);
	}

	public List<ReviewDTO> selectReview(int parkNo) {
		return reviewDAO.selectReview(parkNo);
	}

	public ParkRegiDTO selectOneParkRegi(int parkNo) {
		return regiDAO.selectOneParkRegi(parkNo);
	}

	public List<ParkReserveDTO> selectparkReserve(int parkNo) {
		return parkReserveDAO.selectparkReserve(parkNo);
	}

/*	public List<ParkDTO> userReserve() {
		System.out.println("서비스 실행됨");
		return parkDAO.selectParkList(null);
	}*/

	public Map<String, Object> userReserveForm(int parkNo) {
		ParkDTO parkDTO = parkDAO.selectOnePark(parkNo);
		if (parkDTO == null) {
			throw new RuntimeException("해당 주차장은 존재하지 않습니다.");
		}
		ParkRegiDTO parkRegiDTO = regiDAO.selectOneParkRegi(parkNo);
		if (parkRegiDTO == null) {
			throw new RuntimeException("해당 주차장의 예약정보가 존재하지 않습니다.");
		}
		List<ParkReserveDTO> parkReserveList = parkReserveDAO.selectparkReserve(parkNo);
		List<ReviewDTO> reviewList = reviewDAO.selectReview(parkNo);
		List<ParkImgDTO> parkImageList = parkImgDAO.selectImage(parkNo);
		List<CarTypeDTO> carTypeList = carTypeDAO.selectCarType(parkNo);
		Map<String, Object> map = new HashMap<>();
		map.put("parkDTO", parkDTO);
		map.put("parkRegiDTO", parkRegiDTO);
		map.put("parkReserveList", parkReserveList);
		map.put("reviewList", reviewList);
		map.put("parkImageList", parkImageList);
		map.put("carTypeList", carTypeList);

		return map;
	}

	/**
	 * 회원정보 확인 및 수정
	 */

	public UserDTO selectUserInfo(String userId) {

		return userDAO.selectMemberById(userId);
	}

	public void updateUserInfo(UserDTO userDTO) {
		int result=userDAO.updateUserInfo(userDTO);
		if(result == 0) {
			throw new RuntimeException("회원정보 갱신에 실패하였습니다.");
		}
	}

	public void insertReview(ReviewDTO dto) {
		try {
			int result = reviewDAO.insertReview(dto);
		} catch (Exception e) {
			throw new RuntimeException("리뷰는 한사람당 하나만 등록가능합니다.");
		}
	}

	public void insertReserve(ParkReserveDTO dto) {
		parkReserveDAO.insertReserve(dto);
	}

	public String reserveCheck(ParkReserveDTO dto) {
		System.out.println("서비스호출됨");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String result = "OK";
		try {

			Date date1 = sdf.parse(dto.getReserveStart());
			Date date2 = sdf.parse(dto.getReserveEnd());

			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal1.setTime(date1);
			cal2.setTime(date2);

			int maxCar = carTypeDAO.selectMaxCar(dto);

			// 날짜 차종
			while (cal1.getTimeInMillis() <= cal2.getTimeInMillis()) {
				int s = parkReserveDAO.confirmReserve(new ParkReserveDTO(dto.getParkNo(),sdf.format(cal1.getTime()), dto.getCarType()));
				System.out.println("예약수" + s);
				if (s >= maxCar) {
					result = "No";
				}
				cal1.add(Calendar.HOUR, 1);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println("서비스완료됨");

		return result;
	}

	/**
	 * 	로그인체크하기
	 * */
	public UserDTO loginCheck(UserDTO userDTO) {

		return userDAO.loginCheck(userDTO);
	}

	/**
	 * 아이디로 권한 검색하기 (하나의 아이디에는 여러개의 권한이 있을 수 있다)
	 * */
	public List<AuthorityDTO> selectAuthorityByUserId(String userId) {
		return authoritiesDAO.selectAuthorityByUserId(userId);
	}

	/**
	 * 	이메일을 이용하여 아이디, 비밀번호 찾기
	 * */
	public UserDTO execute(String email) throws Exception {

		UserDTO resultDTO = userDAO.findByEmail(email);

		System.out.println("service에서 resultDTO값 : " + resultDTO);

		if (resultDTO == null)
			throw new RuntimeException();
		// 사용자가 입력한 아이디가 존재하지 않으면 예외 던짐.

		return resultDTO;
	}
	
	/**
	 * 	id 중복 체크
	 * */
	public String idcheck(String userId) {
		UserDTO userDTO = userDAO.idcheck(userId);
		
		return (userDTO!=null) ? "fail" : "ok"; 	//fail  중복, ok 사용가능
	}

	public List<ParkDTO> userMypageReserveList(String userId) {
		return parkReserveDAO.userReserveList(userId);
	}
	
	/**
	 * 	SMS이용하여 본인인증하기
	 * */
	public void insertAuthCode(String userId, String hp, String key) {
			AuthorityDTO authDTO = new AuthorityDTO();
			TempKeyDTO sms = new TempKeyDTO();
			authDTO.setKey(key);
			System.out.println("key : " +key);
			
			authDTO.setRole("ROLE_SELLER");
			authDTO.setUserId(userId);
			authDTO.setHp(hp);
			
			System.out.println("authDTO : " + authDTO.getKey());
			
		 	sms.setUserId(userId);
			sms.setHp(hp);
		 	sms.setKey(key);
		 	
		 	authoritiesDAO.updateKey(authDTO);
			int result = userDAO.SMSAuth(sms);
			
			System.out.println("result 값 : " +result);
			
			if (result == 0) {
				throw new RuntimeException("본인 인증 실패");
			}
	}
	
	/**
	 * 	인증 번호가 일치할 경우 권한을 Seller로 바꿔주기
	 * */
	public void updateAuth(String authKey) {
				int result = authoritiesDAO.updateAuth(authKey);
				
				if (result ==0) 
						throw new RuntimeException("인증 번호가 일치하지 않아 인증에 실패하였습니다.");
	}

	
	/**
	 * 	회원 탈퇴하기 (Auth 테이블)
	 * */
	
	/*public int deleteAuth(String password, String hp) {
		int result = authoritiesDAO.deleteAuth(password, hp);
		
		int count=0;
		
		System.out.println("Auth (service 에서 result ) " +result);
		if (result == 0 ) {
			throw new RuntimeException(hp+ "와 일치하는 회원정보를 찾지 못 하였습니다.(Auth)" );
		} else {
			System.out.println(hp +"와 일치하는 회원 입니다.");
		}
		return result;
	}*/
	
	/**
	 *  회원 탈퇴하기 (SMS 테이블)
	 * */
	
/*	public int deleteSMS(String password, String hp) {
		int result = userDAO.deleteSMS(password, hp);
		
		if (result == 0 ) { 
			throw new RuntimeException(hp+ "와 일치하는 회원정보를 찾지 못 하였습니다.(SMS)");
		}
		return result;
	}*/
	
	/**
	 * 회원 탈퇴시 비밀번호 일치시켜주기 위해서 암호화된 비밀번호 가저오기 
	 * */
	public String selectPassword(String userId) {
			String password = userDAO.selectPassword(userId);
			
			System.out.println("Service에서 비밀번호 : " +password);
			
			return password;
	}
	
	/**
	 * 회원 탈퇴하기 (UserInfo 테이블)
	 * */
	public int deleteUserInfo(String encodePassword) {
		int result = userDAO.deleteUserInfo(encodePassword);
		
		if (result == 0 ) { 
			throw new RuntimeException(encodePassword+ "가 일치 하지 않습니다.");
		}
		return result;
	}
	
	
	public void deleteReserve(int reserveNo) {
		parkReserveDAO.deleteReserve(reserveNo);
	}

	public void updateReserve(ParkReserveDTO dto) {
		parkReserveDAO.updateReserve(dto);
	}
}