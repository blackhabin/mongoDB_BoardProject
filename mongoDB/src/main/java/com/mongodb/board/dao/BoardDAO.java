package com.mongodb.board.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.gridfs.GridFSFile;


@Repository
public class BoardDAO {
	
	
	@Autowired(required=false)
	private GridFsTemplate gridFsTemplate;
	
	 @Inject
	  private MongoTemplate mongoTemplate;

//		// 게시판 리스트
//	 public List<BoardDTO> list() {
//	  	System.out.println(getClass().getSimpleName()+".list()");
//	  	Query query = new Query().with(new Sort(Direction.DESC, "no"));
//	  
//	  
//	  List<BoardDTO> list = mongoTemplate.find(query, BoardDTO.class, "board");
//	    for (BoardDTO dto : list) {
//	        System.out.println(dto.getWritedate());
//	    }
//	  	return list;
	    
//		mongoTempleate.find(조건, 돌려받을 클래스 , 컬렉션)
//	    return mongoTemplate.find(query, BoardDTO.class, "board");
//	 }
//}	
	 
	   // 게시판 리스트
	   public List<Map<String, Object>> list() {
	       System.out.println(getClass().getSimpleName() + ".list()");
	       Query query = new Query().with(new Sort(Direction.DESC, "no"));
	        
	       List<Document> docs = mongoTemplate.find(query, Document.class, "board");
	       List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	        
	       for (Document doc : docs) {
	          Map<String, Object> map = new HashMap<String, Object>();
	          map.putAll(doc);
	          list.add(map);
	        }
	        return list;
	    }
	   
	   // 파일 불러오기(BoardDAO 상세보기에서 호출)
	   // 			(FileService에서도 파일 다운로드를 위해 호출)
	   public GridFSFile getFile(String fileId) {
		    Criteria criteria = Criteria.where("_id").is(fileId);
		    GridFSFile file = gridFsTemplate.findOne(new Query(criteria));
		    return file;
		}

	   
	   // 게시물 상세보기
	   public Map<String, Object> detailPost(int no) {
		   System.out.println("디테일보드 다오");
			Query query = new Query(Criteria.where("no").is(no));
			Document doc = mongoTemplate.findOne(query, Document.class, "board");
			Update update = new Update().inc("hit", 1);
			mongoTemplate.updateFirst(query, update, "board");
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			
			if (doc != null) {
			    map.putAll(doc);
			    map.put("update", update.getUpdateObject());

			    if (doc.get("fileId") != null) {
			        String fileId = doc.get("fileId").toString();
			        GridFSFile file = getFile(fileId);
			        
			        if (file != null) {  // Check if file is null
			            // Convert _id to string
			            Document fileAsDocument = Document.parse(file.toString());
			            fileAsDocument.put("_id", fileAsDocument.get("_id").toString());
			            
			            map.put("file", fileAsDocument);
			        }
			    }
			}
			
			return map;
		}
	    
//	    public int getNextSequence(String seqName) {
//	        Query query = new Query(Criteria.where("_id").is(seqName));
//	        Update update = new Update().inc("seq", 1);
//	        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);
//	        BoardCounterDTO counter = mongoTemplate.findAndModify(query, update, options, 
//	        		BoardCounterDTO.class, "counters");
//	        return counter.getSeq();
//	    }
	    
	    // 게시글 저장시 시퀀스 번호 부여
	    public int getNextSequence(String seqName) {
	        Query query = new Query(Criteria.where("_id").is(seqName));
	        Update update = new Update().inc("seq", 1);
	        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);
	        Document counter = mongoTemplate.findAndModify(query, update, options, Document.class, "counters");
	        return counter.getInteger("seq");
	    }

	    
//	    public void savePost(String title, String content, String writer) {
//	    	int no = getNextSequence("board");
//	    	
//	        BoardDTO post = new BoardDTO();
//	        post.setNo(no);
//	        post.setTitle(title);
//	        post.setContent(content);
//	        post.setWriter(writer);
//	        post.setWritedate(LocalDateTime.now());
//	        post.setHit(0);
//	        mongoTemplate.save(post);
//	    }
	    
	    // 게시물 작성
	    public void savePost(String title, String content, String writer, MultipartFile file) throws IOException {
	    	int no = getNextSequence("board");
	    	
	    	Document doc = new Document();
	    	doc.put("no", no);
	    	doc.put("title", title);
	    	doc.put("content", content);
	    	doc.put("writer", writer);
	    	doc.put("writedate", LocalDateTime.now());
	    	doc.put("modifydate", null);
	    	doc.put("hit", 0);
	    	
	        if (file != null && !file.isEmpty()) {
	            try {
	                String fileId = storeFile(file);
	                doc.put("fileId", fileId);
	            } catch (IOException e) {
	                e.printStackTrace();
	                System.out.println("e.getMessage" + e.getMessage());
	                // handle error
	            }
	        }
	    	
	    	System.out.println("dao저장");
	    	mongoTemplate.save(doc, "board");
	    }
	    
	    // 파일 저장(savePost메서드에서도 호출)
	    public String storeFile(MultipartFile file) throws IOException {
	    	String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
	        String filenameWithoutExtension = FilenameUtils.removeExtension(originalFilename);
	        String extension = FilenameUtils.getExtension(originalFilename);

            // 일련번호 생성
            int serialNumber = generateSerialNumber(); // 이 함수는 일련번호를 생성하고 관리하는 로직을 구현해야 합니다.

            // 일련번호를 추가한 파일 이름
            String filename = filenameWithoutExtension + "_" + serialNumber + "." + extension;
	    	
	    	GridFSFile gridFsFile = gridFsTemplate.store(file.getInputStream(), filename, file.getContentType());

	        // 디렉토리에 파일 저장
	        try {
	        	// 파일 저장 경로 설정
	            Path diretoryPath = 
	            		Paths.get("C:\\file" + File.separator + filename);

	            // StandardCopyOption은 Java NIO의 파일 복사 동작을 정의하는 열거형(enum),
	            // REPLACE_EXISTING: 이 옵션은 대상 위치에 이미 파일이 존재하는 경우, 해당 파일을 덮어쓰는 것을 허용함.
	            Files.copy(file.getInputStream(), diretoryPath, StandardCopyOption.REPLACE_EXISTING);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new IOException(filename  + " 다시 시도해주세요.", e);
	        }

	        return gridFsFile.getId().toString();
	    }
	    
	    // 일련번호 생성 메소드
	    private int generateSerialNumber() {
	        // 현재 시간을 밀리초 단위로 변환
	        long currentTimeMillis = System.currentTimeMillis();
	        return (int) (currentTimeMillis % Integer.MAX_VALUE);
	    }
	    
//	    // 파일만 삭제
//	    public void deleteFile(String fileId) {
//	        gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
//	    }
	    
//	    private void insertFile() throws Exception{
//	        Document doc = new Document("type","image");
//	        doc.append("content_type", "image/png");
//	        doc.append("my_id", "abcd_1234_5678");
//	        doc.append("desc", "한글 입력입니다.");
//	        doc.append("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) );
//
//	        InputStream inStream = new FileInputStream(new File("E:/test.png"));
//	        GridFSBucket gridBucket = GridFSBuckets.create(MongoClien .getDb());   //mongoClient.getDatabase("이름")
//
//	        GridFSUploadOptions uploadOptions = new GridFSUploadOptions().chunkSizeBytes(1024).metadata(doc);
//	        ObjectId fileId = null;
//	        fileId = gridBucket.uploadFromStream("re_name.png", inStream, uploadOptions);  //바꿀파일명을 넣어주세요.
//	        System.out.println("fileId : " + fileId);
//	        inStream.close();
//	    }
//	    
	    
	    
	    // 게시물 수정
	    public void updatePost(int no, String title, String content, String writer,  MultipartFile file) throws IOException {
	    	Query query = new Query(Criteria.where("no").is(no));
	    	System.out.println("업데이트다오");
	    	Update update = new Update();
	    	update.set("no", no);
	    	update.set("title", title);
	    	update.set("content", content);
	    	update.set("writer", writer);
	    	update.set("modifydate", LocalDateTime.now());
	    	
	    	if (file != null && !file.isEmpty()) {
	    		Document doc = mongoTemplate.findOne(query, Document.class, "board");
	    		
	    		if(doc.get("fileId") != null) {
	    			String existingFileId = doc.get("fileId").toString();
	    			gridFsTemplate.delete(new Query(Criteria.where("_id").is(existingFileId)));
	    		}
	    		
	    		String fileId = storeFile(file);
	    		update.set("fileId", fileId);
	    	}
	    	
	    	FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
	    	Document updatePost = mongoTemplate.findAndModify(query, update, options, Document.class, "board");
	    
	    	mongoTemplate.save(updatePost, "board");
	    }

		// 게시물 삭제
	    public Map<String, Object> deletePost(int no) {
	        Query query = new Query(Criteria.where("no").is(no));

	        // 게시글에 첨부된 파일이 있는지 확인
	        Document doc = mongoTemplate.findOne(query, Document.class, "board");
	        if (doc.get("fileId") != null) {
	            String existingFileId = doc.get("fileId").toString();
	            
	            // 디렉토리에서 해당 파일을 먼저 찾음
	            GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(existingFileId)));
	            if (file != null) {
	            	// 일련번호를 추가한 파일 이름
	                String filename = file.getFilename();
	                
	                Path filePath = Paths.get("C:\\file" + File.separator + filename);
	                try {
	                    Files.delete(filePath);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            
	            // GridFS에서 해당 파일을 삭제함
	            gridFsTemplate.delete(new Query(Criteria.where("_id").is(existingFileId)));
	        }

	        // 게시글 삭제
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("delete", mongoTemplate.remove(query, "board"));
	        return map;
	    }


	    // 첨부파일만 삭제
	    public void deleteFile(String fileId, int no) {
	        Query query = new Query(Criteria.where("_id").is(fileId));
	        GridFSFile file = gridFsTemplate.findOne(query);
	        
	        if (file != null) {
	        	String filename = file.getFilename();
                
                Path filePath = Paths.get("C:\\file" + File.separator + filename);
	            try {
	                Files.delete(filePath);
	            } catch (IOException e) {
	                e.printStackTrace();
	                System.out.println("e.getMessage" + e.getMessage());
	            }
	        }
	        
	        gridFsTemplate.delete(query);
	        
	        
	        Query postQuery = new Query(Criteria.where("no").is(no));
	        Update update = new Update().unset("fileId");
	        
	        mongoTemplate.updateFirst(postQuery, update, "board");
	    }
	    
	    // 검색
	    public List<Map<String, Object>> searchPost(String keyword) {
	        Criteria criteria = new Criteria();
	        criteria.orOperator(
	            Criteria.where("title").regex(keyword, "i"),
	            Criteria.where("content").regex(keyword, "i"),
	            Criteria.where("writer").regex(keyword, "i"),
	            Criteria.where("fileName").regex(keyword, "i")
	        );

	        Query query = new Query(criteria).with(new Sort(Direction.DESC, "no"));
	        List<Document> docsboard = mongoTemplate.find(query, Document.class, "board");
	        List<Document> docsfiles = mongoTemplate.find(query, Document.class, "fs.files");
	        List<Map<String, Object>> list = new ArrayList<>();

	        for (Document doc : docsboard) {
	            Map<String, Object> map = new HashMap<>();
	            map.putAll(doc);
	            list.add(map);
	        }
	        
	        for (Document doc : docsfiles) {
	            Map<String, Object> map = new HashMap<>();
	            map.putAll(doc);
	            list.add(map);
	        }

	        return list;
	    }
	    
}