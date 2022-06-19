package com.paymentology.mujokoreconcile.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.paymentology.mujokoreconcile.config.FileService;
import com.paymentology.mujokoreconcile.model.DownstreamTransaction;
import com.paymentology.mujokoreconcile.model.SummaryReport;
import com.paymentology.mujokoreconcile.model.UnmatchTransaction;
import com.paymentology.mujokoreconcile.model.Upload;
import com.paymentology.mujokoreconcile.model.UpstreamTransaction;
import com.paymentology.mujokoreconcile.repo.DownstreamTransactionRepo;
import com.paymentology.mujokoreconcile.repo.UnmatchTransactionRepo;
import com.paymentology.mujokoreconcile.repo.UpstreamTransactionRepo;

@Controller
@RequestMapping("/reconcile")
public class FileController {

	private static final String VIEWS_CREATE_OR_UPDATE_FORM = "payment/createOrUpdateForm";

	@Autowired
	private final UnmatchTransactionRepo unmatchTransactionService;

	@Autowired
	private final DownstreamTransactionRepo downstreamTransactionService;

	@Autowired
	private final UpstreamTransactionRepo upstreamTransactionService;

	public FileController(UnmatchTransactionRepo UnmatchTransactionRepo,
			UpstreamTransactionRepo upstreamTransactionRepo, DownstreamTransactionRepo downstreamTransactionRepo) {
		this.unmatchTransactionService = UnmatchTransactionRepo;
		this.downstreamTransactionService = downstreamTransactionRepo;
		this.upstreamTransactionService = upstreamTransactionRepo;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@Autowired
	FileService fileService;

	@GetMapping("/new")
	public String initCreationForm(Map<String, Object> model) {
		Upload upload = new Upload();
		model.put("upload", upload);
		return VIEWS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/new")
	public String uploadFile(@Valid Upload owner, BindingResult result,
			@RequestParam("fileSource") MultipartFile fileSource, @RequestParam("fileTarget") MultipartFile fileTarget,
			RedirectAttributes redirectAttribute, ModelMap model) {

		if (fileTarget.isEmpty() || fileSource.isEmpty()) {

			redirectAttribute.addFlashAttribute("message", "Please provide file to reconcile!!");
			return "redirect:/reconcile/new";
		}
		else if (fileTarget.getOriginalFilename().equalsIgnoreCase(fileSource.getOriginalFilename())) {

			redirectAttribute.addFlashAttribute("message", "Both are same files, Only compare from different file!");
			return "redirect:/reconcile/new";

		}
		else if (!fileTarget.getOriginalFilename().endsWith(".csv")
				|| !fileTarget.getOriginalFilename().endsWith(".csv")) {
			redirectAttribute.addFlashAttribute("message", "Only allow csv file!");
			return "redirect:/reconcile/new";

		}

		Map<String, UpstreamTransaction> upstreamMap = new HashMap<String, UpstreamTransaction>();
		Map<String, DownstreamTransaction> downstreamMap = new HashMap<String, DownstreamTransaction>();

		AtomicLong sourceCount = new AtomicLong();
		AtomicLong matchingSource = new AtomicLong();
		AtomicLong unmatchSource = new AtomicLong();

		AtomicLong targetCount = new AtomicLong();
		AtomicLong matchingTarget = new AtomicLong();
		AtomicLong unmatchTarget = new AtomicLong();
		// reset for new process
		unmatchTransactionService.deleteAll();

		// Step 1, read file and store
		try (Reader reader = new BufferedReader(new InputStreamReader(fileSource.getInputStream()))) {
			// create csv bean reader
			CsvToBean<UpstreamTransaction> csvToBean = new CsvToBeanBuilder(reader).withType(UpstreamTransaction.class)
					.withSkipLines(1).withIgnoreLeadingWhiteSpace(true).build();
			List<UpstreamTransaction> transactions = csvToBean.parse();
			for (UpstreamTransaction upstreamTransaction : transactions) {
				sourceCount.getAndIncrement();
				// duplicate record or invalid data
				if (upstreamTransaction.getWalletReference()==null||upstreamMap
						.containsKey(upstreamTransaction.getWalletReference() + upstreamTransaction.getAmount())) {
					unmatchSource.getAndIncrement();
					UnmatchTransaction unmatchTransaction = new UnmatchTransaction();
					BeanUtils.copyProperties(upstreamTransaction, unmatchTransaction);
					unmatchTransaction.setDataSource(fileSource.getOriginalFilename());
					unmatchTransactionService.save(unmatchTransaction);
				}
				else {
					upstreamMap.put(upstreamTransaction.getWalletReference() + upstreamTransaction.getAmount(),
							upstreamTransaction);
				}
				// For Demo, no need to db
			}

		}
		catch (Exception ex) {

		}
		// Step 2, read target file, store and compare with source
		try (Reader reader = new BufferedReader(new InputStreamReader(fileTarget.getInputStream()))) {
			// create csv bean reader
			CsvToBean<DownstreamTransaction> csvToBean = new CsvToBeanBuilder(reader)
					.withType(DownstreamTransaction.class).withSkipLines(1).withIgnoreLeadingWhiteSpace(true).build();
			List<DownstreamTransaction> transactions = csvToBean.parse();

			for (DownstreamTransaction downstreamTransaction : transactions) {
				targetCount.getAndIncrement();

				// duplicate record or invalid data
				if (downstreamTransaction.getWalletReference()==null||downstreamMap
						.containsKey(downstreamTransaction.getWalletReference() + downstreamTransaction.getAmount())) {
					unmatchTarget.getAndIncrement();
					UnmatchTransaction unmatchTransaction = new UnmatchTransaction();
					BeanUtils.copyProperties(downstreamTransaction, unmatchTransaction);
					unmatchTransaction.setDataSource(fileTarget.getOriginalFilename());
					unmatchTransactionService.save(unmatchTransaction);
					continue;
				}
				else {
					downstreamMap.put(downstreamTransaction.getWalletReference() + downstreamTransaction.getAmount(),
							downstreamTransaction);
				}

				// For Demo, no need to db
			}

			// Step 3 compare source for unmatch
			for (String walletrefAndTrxTypeAndAmount : upstreamMap.keySet()) {
				// compare with downstreamMap if Any not match then put unmatch
				if (downstreamMap.containsKey(walletrefAndTrxTypeAndAmount)) {
					matchingSource.getAndIncrement();
				}
				else {
					unmatchSource.getAndIncrement();
					UnmatchTransaction unmatchTransaction = new UnmatchTransaction();
					BeanUtils.copyProperties(upstreamMap.get(walletrefAndTrxTypeAndAmount), unmatchTransaction);
					unmatchTransaction.setDataSource(fileSource.getOriginalFilename());
					unmatchTransactionService.save(unmatchTransaction);
				}
			}

			// Step 4 compare target a for unmatch
			for (String walletrefAndTrxTypeAndAmount : downstreamMap.keySet()) {

				if (upstreamMap.containsKey(walletrefAndTrxTypeAndAmount)) {
					matchingTarget.getAndIncrement();
				}
				else {
					unmatchTarget.getAndIncrement();
					UnmatchTransaction unmatchTransaction = new UnmatchTransaction();
					BeanUtils.copyProperties(downstreamMap.get(walletrefAndTrxTypeAndAmount), unmatchTransaction);
					unmatchTransaction.setDataSource(fileTarget.getOriginalFilename());
					unmatchTransactionService.save(unmatchTransaction);
				}
			}

			SummaryReport summary = new SummaryReport();
			summary.setFileNameSource(fileSource.getOriginalFilename());
			summary.setTotalRecordSource(sourceCount.get());
			summary.setUnmatchSource(unmatchSource.get());
			summary.setMatchSource(matchingSource.get());

			summary.setFileNameTarget(fileTarget.getOriginalFilename());
			summary.setTotalRecordTarget(targetCount.get());
			summary.setUnmatchTarget(unmatchTarget.get());
			summary.setMatchTargete(matchingTarget.get());
			System.out.println(summary);
			model.put("summary", summary);

		}
		catch (Exception ex) {

		}

		// redirectAttributes.addFlashAttribute("message",
		// "You Successfully Uploaded" + fileSource.getOriginalFilename() + "!");
		return "payment/summary.html";
	}

	@GetMapping("/report/{source}")
	public String report(@PathVariable("source") String source, ModelMap model) {

		List<UnmatchTransaction> listUnmatch = unmatchTransactionService
				.findByDataSourceIsOrderByCreatedDateAsc(source);

		List<UnmatchTransaction> listUnmatch2 = unmatchTransactionService
				.findByDataSourceIsNotOrderByCreatedDateAsc(source);

		model.put("listUnmatch", listUnmatch);
		if (listUnmatch.size() > 0)
			model.put("fileName1", listUnmatch.get(0).getDataSource());
		else
			model.put("fileName1", "");

		model.put("listUnmatch2", listUnmatch2);
		if (listUnmatch2.size() > 0)
			model.put("fileName2", listUnmatch2.get(0).getDataSource());
		else
			model.put("fileName2", "");

		return "payment/unmatchList.html";

	}

	@ResponseBody
public ModelAndView resolveException(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, Object o, Exception e) {
    if (e instanceof MaxUploadSizeExceededException) {
        ModelAndView modelAndView = new ModelAndView("inline-error");
        modelAndView.addObject("error", 
        "Error: Your file size is too large to upload. Please upload a file of size < 5 MB and  continue. ");
    return modelAndView;
    }
    e.printStackTrace();
    return new ModelAndView("500");
}

}
