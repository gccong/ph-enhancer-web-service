package org.ncpsb.phoenixcluster.enhancer.webservice.api.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.ncpsb.phoenixcluster.enhancer.webservice.domain.PageOfScoredPSM;
import org.ncpsb.phoenixcluster.enhancer.webservice.domain.ScoredPSM;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.ScoredPSMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/example/v1/scoredpsms")
@Api(tags = {"scoredPSMs"})
public class ScoredPSMController extends AbstractRestHandler {

    @Autowired
    private ScoredPSMService scoredPSMService;

    @RequestMapping(value = "",
            method = RequestMethod.GET
//            ,produces = {"application/json", "application/xml"}
            ,produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all scored PSMs.", notes = "The list is paginated. You can provide a page number (default 1) and a page size (default 100)")
    public
    @ResponseBody
    PageOfScoredPSM getScoredPSMs(@ApiParam(value = "The page number, start at 1", required = true)
                                      @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                               @ApiParam(value = "Tha page size", required = true)
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                  @ApiParam(value = "The sortField", required = true)
                                  @RequestParam(value = "sortFiled", required = true, defaultValue = DEFAULT_SORT_FIELD) String sortField,
                                  @ApiParam(value = "The sort direction", required = true)
                                  @RequestParam(value = "sortDirection", required = true, defaultValue = DEFAULT_SORT_DIRECTION) String sortDirection,
                               HttpServletRequest request, HttpServletResponse response) {
//        return this.clusterService.getAllClusterIDs(page, size,null, null);
//        List<ScoredPSM> scoredPSMs = this.scoredPSMService.getScoredPSMs(page, size,null, null);
        assert(page > 0);
        assert(size > 0);
        List<ScoredPSM> scoredPSMs = this.scoredPSMService.getScoredPSMs(page, size,sortField, sortDirection);
        Integer totalElements = this.scoredPSMService.totalScoredPSM();
        Integer totalPages = (int) Math.ceil((totalElements + 0.0)/size);
        PageOfScoredPSM pageOfScoredPSM = new PageOfScoredPSM(size, page, totalElements, totalPages, sortField, sortDirection,scoredPSMs);

        return pageOfScoredPSM;
    }

    @RequestMapping(value = "/{title}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single scored PSM.", notes = "You have to provide a valid spectrum title.")
    public
    @ResponseBody
    ScoredPSM getScoredPSM(@ApiParam(value = "The ID of the cluster.", required = true)
                             @PathVariable("id") String id,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        ScoredPSM scoredPSM = this.scoredPSMService.getPSMByTitle(id, null);
//        checkResourceFound(cluster);
        //todo: http://goo.gl/6iNAkz
        return scoredPSM;
    }

}
