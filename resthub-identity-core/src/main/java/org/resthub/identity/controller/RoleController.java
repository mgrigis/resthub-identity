package org.resthub.identity.controller;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.common.exception.NotFoundException;

import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.service.RoleService;
import org.resthub.identity.service.UserService;
import org.resthub.web.controller.ServiceBasedRestController;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller @RequestMapping("/api/role")
public class RoleController extends ServiceBasedRestController<Role, Long, RoleService> {

    @Inject @Named("userService")
    protected UserService userService;

    @Inject @Named("roleService") @Override
    public void setService(RoleService service) {
        this.service = service;
    }

    /** Override this methods in order to secure it **/
    @Secured({ "IM_ROLE_ADMIN" }) @Override 
    public Role create(@RequestBody Role role) {
        return super.create(role);
    }

    /** Override this methods in order to secure it **/
    @Secured({ "IM_ROLE_ADMIN" }) @Override
    public Role update(@PathVariable("id") Long id, @RequestBody Role role) {
        return super.update(id, role);
    }
    
    /** Override this methods in order to secure it **/
    @Secured({ "IM_ROLE_ADMIN", "IM_ROLE_READ" }) @Override
    public Iterable<Role> findAll() {
        return super.findAll();
    }
    
    /** Override this methods in order to secure it **/
    @Secured({ "IM_ROLE_ADMIN", "IM_ROLE_READ" }) @RequestMapping(value = "/findAllPerPage", method = RequestMethod.GET) @Override
    public Page<Role> findPaginated(@RequestParam(value="page", required=false) Integer page, @RequestParam(value="size", required=false) Integer size) {
        return super.findPaginated(page, size);
    }
    
    /** Override this methods in order to secure it **/
    @Secured({ "IM_ROLE_ADMIN", "IM_ROLE_READ" }) @Override
    public Role findById(@PathVariable("id") Long id) {
        return super.findById(id);
    }
    
    /** Override this methods in order to secure it **/
    @Secured({ "IM_ROLE_ADMIN" }) @Override
    public void delete() {
        super.delete();
    }
    
    /** Override this methods in order to secure it **/
    @Secured({ "IM_ROLE_ADMIN" }) @Override
    public void delete(@PathVariable("id") Long id) {
        super.delete(id);
    }

    /**
     * Gets all the users that have a role, direct or inherited.
     * 
     * @param filters
     *            A list of roles to look for.
     * @return A list of users having at least one of the roles defined as
     *         parameter.
     */

    @Secured({ "IM-ADMIN" }) @RequestMapping(method = RequestMethod.GET, value = "{name}/users") @ResponseBody 
    public List<User> findAllUsersWithRole(@PathVariable("name") String name) {
        List<User> usersWithRoles = this.userService.findAllUsersWithRoles(Arrays.asList(name));
        if (usersWithRoles == null) {
            throw new NotFoundException();
        }
        return usersWithRoles;
    }
}
