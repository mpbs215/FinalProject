package kosta.mvc.model.dto;

import java.util.List;

public class ParkReserveDTO {
	private int reserveNo;
	private String userId;
	private int parkNo;
	private String reserveStart;
	private String reserveEnd;
	private String carType;
	
	private UserDTO userDto;
	private ParkDTO parkDto;	
	
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	private List<ParkDTO> park;
	
	public List<ParkDTO> getPark() {
		return park;
	}
	public void setPark(List<ParkDTO> park) {
		this.park = park;
	}
	
	public ParkReserveDTO(int park_No,String reserveStart,String carType) {
		this.parkNo = park_No;
		this.reserveStart = reserveStart;
		this.carType = carType;
	}
	
	public ParkReserveDTO(int reserveNo, String userId, int parkNo, String reserveStart, String reserveEnd,
			String carType) {
		super();
		this.reserveNo = reserveNo;
		this.userId = userId;
		this.parkNo = parkNo;
		this.reserveStart = reserveStart;
		this.reserveEnd = reserveEnd;
		this.carType = carType;
	}
	public ParkReserveDTO(int reserveNo, String userId, int parkNo, String reserveStart, String reserveEnd,
			String carType, List<ParkDTO> park) {
		super();
		this.reserveNo = reserveNo;
		this.userId = userId;
		this.parkNo = parkNo;
		this.reserveStart = reserveStart;
		this.reserveEnd = reserveEnd;
		this.carType = carType;
		this.park = park;
	}
	public ParkReserveDTO() {
		super();
	}
	public int getReserveNo() {
		return reserveNo;
	}
	public void setReserveNo(int reserveNo) {
		this.reserveNo = reserveNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getParkNo() {
		return parkNo;
	}
	public void setParkNo(int parkNo) {
		this.parkNo = parkNo;
	}
	public String getReserveStart() {
		return reserveStart;
	}
	public void setReserveStart(String reserveStart) {
		this.reserveStart = reserveStart;
	}
	public String getReserveEnd() {
		return reserveEnd;
	}
	public void setReserveEnd(String reserveEnd) {
		this.reserveEnd = reserveEnd;
	}	
	public UserDTO getUserDto() {
		return userDto;
	}
	public void setUserDto(UserDTO userDto) {
		this.userDto = userDto;
	}
	public ParkDTO getParkDto() {
		return parkDto;
	}
	public void setParkDto(ParkDTO parkDto) {
		this.parkDto = parkDto;
	}
	@Override
	public String toString() {
		return "ParkReserveDTO [reserveNo=" + reserveNo + ", userId=" + userId + ", parkNo=" + parkNo
				+ ", reserveStart=" + reserveStart + ", reserveEnd=" + reserveEnd + ", carType=" + carType + ", park="
				+ park + "]";
	}
	
	
}