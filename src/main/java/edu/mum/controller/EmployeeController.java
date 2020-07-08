package edu.mum.controller;

import edu.mum.domain.Employee;
import edu.mum.exception.UploadImageException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import org.apache.commons.io.FilenameUtils;

@Controller
public class EmployeeController {

	private static final Log logger = LogFactory.getLog(EmployeeController.class);

	@Autowired
    ServletContext servletContext;

	@GetMapping(value = { "/", "employee_input" })
	public String inputEmployee(@ModelAttribute("employee") Employee employee) {
		return "EmployeeForm";
	}

	@PostMapping(value = "/employee_input")
	public String saveEmployee(@Valid
                                       Employee emp, BindingResult bindingResult,
                               Model model) throws UploadImageException, IOException {

		if (bindingResult.hasErrors()) {
			return "EmployeeForm";
		}

		String[] suppressedFields = bindingResult.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempt to bind fields that haven't been allowed in initBinder(): "
					+ StringUtils.addStringToArray(suppressedFields, ", "));
		}

		MultipartFile employeeImage = emp.getEmployeeImage();
		if (employeeImage != null && !employeeImage.isEmpty()) {
			String extension = getExtension(employeeImage.getOriginalFilename());
			List<String> extensions = new ArrayList<>();
			extensions.add(".jpg");
			extensions.add(".png");
			extensions.add(".jpeg");
			extensions.add(".gif");
			if(!extensions.contains(extension)){
				throw new UploadImageException("File must be image");
			}
			String imgFileName = emp.getId() + extension;
				String rootDirectory = servletContext.getRealPath("/");
				File dir = new File(rootDirectory + "images" );
				if(!dir.exists()){
					dir.mkdir();
				}
				employeeImage.transferTo(new File(dir, imgFileName));
			emp.setImageFileName(imgFileName);
		}

		model.addAttribute("employee", emp);

		return "EmployeeDetails";
	}

	private String getExtension(String fileName) {
		String extension = ".png";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i);
		}
		return extension;
//		return FilenameUtils.getExtension(employeeImage.getOriginalFilename());
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// binder.setDisallowedFields(new String[]{"firstName"});
	}
}
