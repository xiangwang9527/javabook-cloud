package cn.javabook.chapter09.rbac.interceptor;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 拦截处理器（为了后续章节能够继续，需要将此类中的代码全部注释。当演示RBAC自定义拦截器时可以取消注释）
 *
 */
@Aspect
@Component
public class InterceptorHandler {
//	@Resource
//	private UserService userService;
//
//	@Resource
//	private RoleService roleService;
//
//	@Resource
//	private PermissionService permissionService;
//
//	/*
//	 * 拦截controller包下面的所有类中，有@RequestMapping注解的方法
//	 */
//	@Pointcut("execution(* com.java.book.chapter04.controller..*.*(..)) " +
//			"&& @annotation(org.springframework.web.bind.annotation.RequestMapping)")
//	public void controllerMethodPointcut() {
//	}
//
//	/**
//	 * 拦截器具体实现
//	 */
//	@Around("controllerMethodPointcut()")
//	public Object Interceptor(final ProceedingJoinPoint pjp) {
//		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = sra.getRequest();
//		Map<String, String> argsMap = new HashMap<String, String>();
//		Enumeration<String> em = request.getParameterNames();
//		String methodName = pjp.getSignature().getName();
//
//		// 加入参数
//		while (em.hasMoreElements()) {
//			String paramName = em.nextElement();
//			String value = request.getParameter(paramName);
//			argsMap.put(paramName, value);
//		}
//		String username = argsMap.get("username");
//		String platform = argsMap.get("platform");
//		String timestamp = argsMap.get("timestamp");
//		String signature = argsMap.get("signature");
//		// 验证参数
//		if (null == platform || null == timestamp || null == signature) {
//			return "params required";
//		}
//
//		// 后端生成签名：platform = "javabook", timestamp = "159123456789", signature = a8354bc1b54a39528e81c549ec373c14
//		String sign = DigestUtils.md5DigestAsHex((platform + timestamp).getBytes());
//		if (!signature.equalsIgnoreCase(sign)) {
//			return "signature error";
//		}
//
//		// 获取切面标记
//		Signature signatureObject = pjp.getSignature();
//		if (!(signatureObject instanceof MethodSignature)) {
//			throw new IllegalArgumentException("this annotation can be applied on method only");
//		}
//
//		// 获取用户信息
//		SysUser user = userService.queryByUsername(username);
//		if (null == user) {
//			return "user is not exist";
//		}
//
//		/*
//		 * 获得用户拥有的全部角色
//		 */
//		// 用户所属的角色
//		Set<SysRole> userRoleSet = new HashSet<>();
//		// 用户所拥有的全部角色
//		Set<SysRole> userAllRoleSet = new HashSet<>();
//		// 查询这些角色的全部父角色
//		StringBuilder roleIds = new StringBuilder();
//		Set<String> userRoleNameSet = new HashSet<>();
//		// 用户-组-角色
//		List<SysRole> ugr = roleService.queryUGRByUserId(user.getId());
//		if (null != ugr && ugr.size() > 0) {
//			userRoleSet.addAll(ugr);
//		}
//		// 用户-角色
//		List<SysRole> ur = roleService.queryURByUserId(user.getId());
//		if (null != ur && ur.size() > 0) {
//			userRoleSet.addAll(ur);
//		}
//		// 合并全部角色
//		for (SysRole role : userRoleSet) {
//			List<SysRole> list = roleService.queryParentsById(role.getParentids());
//			if (null != list && list.size() > 0) {
//				list.forEach(r -> {
//					userAllRoleSet.add(r);
//					userAllRoleSet.add(role);
//				});
//			}
//		}
//		// 查询这些角色的全部权限
//		userAllRoleSet.forEach(r -> {
//			roleIds.append(r.getId()).append(",").append(r.getParentids());
//			userRoleNameSet.add(r.getName());
//		});
//		List<SysPermission> rolePermissions = permissionService.queryByMultiRoleIds(roleIds.toString());
//
//		/*
//		 * 获得用户拥有的全部权限
//		 */
//		Set<String> userPermissionSet = new HashSet<>();
//		// 用户-组-角色-权限
//		List<SysPermission> ugrp = permissionService.queryUGRPByUserId(user.getId());
//		if (null != ugrp && ugrp.size() > 0) {
//			ugrp.forEach(r -> userPermissionSet.add(r.getPath()));
//		}
//		// 用户-角色-权限
//		List<SysPermission> urp = permissionService.queryURPByUserId(user.getId());
//		if (null != urp && urp.size() > 0) {
//			urp.forEach(r -> userPermissionSet.add(r.getPath()));
//		}
//		// 用户-权限
//		List<SysPermission> up = permissionService.queryUPByUserId(user.getId());
//		if (null != up && up.size() > 0) {
//			up.forEach(r -> userPermissionSet.add(r.getPath()));
//		}
//		// 合并之前的结果
//		if (null != rolePermissions && rolePermissions.size() > 0) {
//			rolePermissions.forEach(r -> userPermissionSet.add(r.getPath()));
//		}
//
//		// 权限判断
//		Object target = pjp.getTarget();
//		Class<?> clazz = target.getClass();
//		MethodSignature methodSignature = (MethodSignature) signatureObject;
//		try {
//			Method method = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
//			// 获取注解类
//			PreAuthorize preAuthorize = method.getDeclaredAnnotation(PreAuthorize.class);
//			RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
//			if (null != preAuthorize) {
//				// 只有当用户具备此角色且访问路径与权限中的path相等时才能认为具有该资源的操作权限
//				if (userRoleNameSet.contains(preAuthorize.role()) && userPermissionSet.contains(requestMapping.value()[0])) {
//					System.out.println(username + " ==> " + clazz.getSimpleName() + " - " + method.getName() + " - " +
//							preAuthorize.role() + " - " + requestMapping.value()[0]);
//					// 继续往下执行
//					return pjp.proceed();
//				} else {
//					return "permission denied";
//				}
//			}
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//
//		return "failure";
//	}
}
