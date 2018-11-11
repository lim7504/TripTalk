<%@page import="Servlet.ConnectDB"%>
<%@page import="oreilly.servlet.MultipartRequest"%>
<%@page import="oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@ page import = "java.io.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	String type = request.getParameter("type");

	if(!(type == null)) {
		if(type.equals("blogIn")) {
			ConnectDB instance = new ConnectDB();
			String returns = Integer.toString(instance.tt_blogIn());
			out.print(returns);
		} else if(type.equals("blogOut")) {
			ConnectDB instance = new ConnectDB();
			String blog_id = request.getParameter("blogId");
			int blogId = 0;
			if(!(blog_id == null)) {
				if(!blog_id.equals("")) {
					blogId = Integer.parseInt(blog_id);
				}
			}
			String returns = instance.tt_blogOut(blogId);
			out.print(returns);
		} else if(type.equals("blogSearch")) {
			ConnectDB instance = new ConnectDB();
			String blog_id = request.getParameter("blogId");
			int blogId = 0;
			if(!(blog_id == null)) {
				if(!blog_id.equals("")) {
					blogId = Integer.parseInt(blog_id);
				}
			}
			String returns = instance.tt_blog_search(blogId);
			out.print(returns);
		} else if(type.equals("blogDetailSearch")) {
			ConnectDB instance = new ConnectDB();
			String blog_id = request.getParameter("blogId");
			int blogId = 0;
			if(!(blog_id == null)) {
				if(!blog_id.equals("")) {
					blogId = Integer.parseInt(blog_id);
				}
			}
			String returns = instance.tt_blog_DetailSearch(blogId);
			out.print(returns);
		} else if(type.equals("blogAllSearch")) {
			ConnectDB instance = new ConnectDB();
			String returns = instance.tt_blog_AllSearch();
			out.print(returns);
		} else if(type.equals("blogInsert")) {
			ConnectDB instance = new ConnectDB();
			String blog_id = request.getParameter("blogId");
			String title = request.getParameter("blogTitle");
			String content = request.getParameter("blogContent");
			String writer = request.getParameter("blogWriter");
			int blogId = 0;
			if(!(blog_id == null)) {
				if(!blog_id.equals("")) {
					blogId = Integer.parseInt(blog_id);
				}
			}
			String returns = instance.tt_blog_insert(blogId,title,content,writer);
			out.print(returns);
		} else if(type.equals("blogImageReset")) {
			ConnectDB instance = new ConnectDB();
			String dir = application.getRealPath("/upload/");
			String blog_id = request.getParameter("blogId");
			int blogId = 0;
			if(!(blog_id == null)) {
				if(!blog_id.equals("")) {
					blogId = Integer.parseInt(blog_id);
				}
			}

			String deletefile = instance.tt_blog_reset(blogId);
			deletefile = deletefile.replaceAll("/upload/","");
			if(!deletefile.equals("fail")) {
				String[] split = deletefile.split("!@#");
				for(int i = 0; i < split.length; i++) {
					File f = new File(dir + split[i]); // 파일 객체생성
					System.out.println("파일명 : " + dir+split[i]);
					   if( f.exists()){
							System.out.println("삭제 동작" + (i+1));
						   f.delete();
						   System.out.println("delete complete");
					   }
				}
			}

		}
	} else {

		request.setCharacterEncoding("UTF-8");
		String dir = application.getRealPath("/upload/");   //이미지 저장될 폴더명
		String a = request.getSession().getServletContext().getRealPath("/upload");

		int max = 10*1024*1024;

		MultipartRequest mr = new MultipartRequest(request, dir, max, "UTF-8", new DefaultFileRenamePolicy());
		String orgFileName = mr.getOriginalFileName("image");
		String saveFileName = mr.getFilesystemName("image");  // upload에 저장된 사진파일 명

		request.setCharacterEncoding("UTF-8");
		String blog_id = mr.getParameter("blogId");
		int blogId = 0;
		if(!(blog_id == null)) {
			if(!blog_id.equals("")) {
				blogId = Integer.parseInt(blog_id);
			}
		}
		String imageNum = "";
		imageNum = mr.getParameter("imageNum");
		int number = 0;

		String image_root = "/upload/" + saveFileName;

		System.out.println(imageNum);
		ConnectDB instance = new ConnectDB();

		String returns = instance.tt_blog_image(blogId, image_root, imageNum);
		out.print(returns);
	}

%>
