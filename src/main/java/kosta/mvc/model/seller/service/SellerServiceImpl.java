package kosta.mvc.model.seller.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kosta.mvc.model.dao.CarTypeDAO;
import kosta.mvc.model.dao.ParkDAO;
import kosta.mvc.model.dao.ParkImgDAO;
import kosta.mvc.model.dao.ParkReserveDAO;
import kosta.mvc.model.dao.RegiDAO;
import kosta.mvc.model.dto.CarTypeDTO;
import kosta.mvc.model.dto.ParkDTO;
import kosta.mvc.model.dto.ParkImgDTO;
import kosta.mvc.model.dto.ParkRegiDTO;
import kosta.mvc.model.dto.ParkReserveDTO;
import kosta.mvc.model.dto.SearchFilterDTO;

@Transactional
@Service
public class SellerServiceImpl {

	@Autowired
	private RegiDAO regiDAO;
	@Autowired
	private ParkDAO parkDAO;
	@Autowired
	private ParkImgDAO parkImgDAO;
	@Autowired
	private CarTypeDAO carTypeDAO;
	@Autowired
	private ParkReserveDAO parkReserveDAO;

	/**
	 * ������ ���
	 */
	public void sellerParkRegist(ParkDTO parkDto, CarTypeDTO carTypeDto, ParkImgDTO parkImgDto, ParkRegiDTO parkRegiDto, String imgPath, List<MultipartFile> mf) throws Exception{		
		System.out.println("��������");
		// �������� parkNo�� �װ� ���̺� ���� ������ �ֱ� ����. 
		int parkNo = parkDAO.selectParkNo();
		parkDto.setParkNo(parkNo);
		carTypeDto.setParkNo(parkNo);
		parkRegiDto.setParkNo(parkNo);
		parkImgDto.setParkNo(parkNo);
		int resultPark = parkDAO.insertPark(parkDto);
		System.out.println("resultPark"+resultPark);
		if(resultPark == 0) {
			throw new RuntimeException("������ ��� ����");
		}		
		
		int resultParkRegi = regiDAO.insertParkRegi(parkRegiDto);
		System.out.println("resultParkRegi"+resultParkRegi);
		if(resultParkRegi == 0) {
			throw new RuntimeException("�����忹��ð� ��� ����");
		}
		
		System.out.println("���� ��� ��");
		/** ����, �� �� */
		List<String> carTypeList = carTypeDto.getCarTypeList();
		List<Integer> maxCarList = carTypeDto.getMaxCarList();
				
		for(int i=0; i<carTypeList.size(); i++) {
			String carType = carTypeList.get(i);
			carTypeDto.setCarType(carType);
			System.out.println("=============carType="+carType);
			int maxCar = maxCarList.get(i);
			carTypeDto.setMaxCar(maxCar);
			System.out.println("=============maxCar="+maxCar);
			
			int resultCarType = carTypeDAO.insertCarType(carTypeDto); 
			System.out.println("resultCarType"+resultCarType);
			if(resultCarType == 0) {
				throw new RuntimeException();
			}
		}
		System.out.println("���� ��� �� �̹��� ���ε� ��");

			   
		/** �̹��� ���ε� */		 
		MultipartFile file = null;
		String originalFilename = null; // �������� ���� �̸�
		String fileName = null; // �����̸� �ߺ� �������� �ٲ� �̸�(����� �̸�)
		double fileSize = 0; // ���� ������
		//String fileExtension = null; // ���� Ȯ����
		int fileCnt = mf.size(); // ���� ����
        	
		for(int i=0; i<mf.size(); i++) {
			
			file = mf.get(i);
			
			parkImgDto.setFiles(mf.get(i));
			
			originalFilename = mf.get(i).getOriginalFilename();
			fileName= System.currentTimeMillis() + "_" + originalFilename;			
			fileSize = mf.get(i).getSize();
			//fileExtension = FilenameUtils.getExtension(originalFilename).toLowerCase();
			
			parkImgDto.setImgPath(fileName);
						
			// DAO ȣ��
			int resultParkImg = parkImgDAO.insertImg(parkImgDto);
			System.out.println("resultParkImg"+resultParkImg);
			if(resultParkImg == 0) {
				throw new RuntimeException();
			}
						
			file.transferTo(new File(imgPath+ "/" + fileName));// �̹��� ����
			System.out.println("�̹��� ���ε� ��");

		}		
	}
		
	/**
	 * ����� ������ ����Ʈ
	 */	
	public List<ParkDTO> sellerParkList(String userId) {
		return parkDAO.selectParkList(userId);
	}
/*	���������̼� �ּ� ó��
 * 	public List<ParkDTO> sellerParkList(String userId, int cPage, int numPerPage) {
		return parkDAO.selectParkList(userId, cPage, numPerPage);
	}*/
	
	/**
	 * ���������̼� ���� ������ �� ī��Ʈ
	 */
/*	public int parkCnt(String userId, int cPage, int numPerPage) {
		List<ParkDTO> parkList = parkDAO.selectParkList(userId, cPage, numPerPage);	
		return parkList.size();
	}*/
	
	/**
	 * ������ �ϳ� ����
	 */
	public int sellerParkDelete(int parkNo) {
				
		int resultPark = parkDAO.deletePark(parkNo);
		if(resultPark == 0) {
			throw new RuntimeException();
		}		
		
		return resultPark;
	}
	
	/**
	 * ������ �ѹ��� ������ ����
	 */	
	public int sellerParksDelete(List<String> parkNos) {
		
		int parkNo=0;
		int deletesResult=0;
		for(int i=0; i<parkNos.size(); i++) {
			parkNo= Integer.parseInt(parkNos.get(i));
			
			int deleteResult = sellerParkDelete(parkNo);	
			if(deleteResult == 0) {
				throw new RuntimeException();
			}
			
			if(i==parkNos.size()-1) {
				deletesResult = deleteResult;
			}		
		}
		return deletesResult;
	}
	
	
	/**
	 * ����� �����忡 ���� ���� ����Ʈ - ����
	 */
	public List<ParkReserveDTO> sellerReserveList(String userId){		
		return parkDAO.sellerReserveList(userId);
	}
/*	public List<ParkReserveDTO> sellerReserveList(String userId, int cPage, int numPerPage){		
		return parkDAO.sellerReserveList(userId, cPage, numPerPage);
	}*/
		
	/**
	 * ����� �����忡 ���� ���� ����Ʈ - �̷�
	 */
	public List<ParkReserveDTO> sellerReserveListLoad(String userId){
		return parkDAO.sellerReserveListLoad(userId);
	}
/*	public List<ParkReserveDTO> sellerReserveListLoad(String userId, int cPage, int numPerPage){
		return parkDAO.sellerReserveListLoad(userId, cPage, numPerPage);
	}*/

/*	
	*//**
	 * ���������̼� ���� ���� �� ī��Ʈ - ����
	 *//*
	public int sellerReserveListCnt(String userId, int cPage, int numPerPage) {
		List<ParkReserveDTO> sellerReserveList = parkDAO.sellerReserveList(userId, cPage, numPerPage);	
		return sellerReserveList.size();
	}
	
	*//**
	 * ���������̼� ���� ���� �� ī��Ʈ - �̷�
	 *//*
	public int sellerReserveListLoadCnt(String userId, int cPage, int numPerPage) {
		List<ParkReserveDTO> sellerReserveListLoad = parkDAO.sellerReserveListLoad(userId, cPage, numPerPage);	
		return sellerReserveListLoad.size();
	}
*/
	
	
	/**
	 * ���� ����(���)
	 */
	public void sellerReserveDelete(int reserveNo) {
		int re = parkReserveDAO.sellerReserveDelete(reserveNo);
		if(re==0) {
			throw new RuntimeException();
		}
	}

	public List<ParkDTO> callStats(String startDate, String endDate,String userId) {
		List<ParkDTO> list = null;
		try {
			list = parkDAO.selectProfit(startDate, endDate,userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	} 
	
	
	

	

/*	public int sellerParkDelete(int parkNo) {
		return parkDAO.sellerParkDelete(parkNo);
	}

	public int sellerParksDelete(int[] parkNos) {
		int count=0;
		for(int parkNo:parkNos) {
			int re=parkDAO.sellerParkDelete(parkNo);
			if(re==0) {
				throw new RuntimeException();
			}else {
				count++;
			}
		}
		return count;
	}*/
}