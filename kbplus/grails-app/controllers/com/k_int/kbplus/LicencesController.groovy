package com.k_int.kbplus

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.*
import org.elasticsearch.groovy.common.xcontent.*
import groovy.xml.MarkupBuilder
import grails.plugin.springsecurity.annotation.Secured
import com.k_int.kbplus.auth.*;



class LicencesController {

  def springSecurityService

  static allowedMethods = [create: ['GET', 'POST'], edit: ['GET', 'POST'], delete: 'POST']

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() {
      redirect action: 'list', params: params
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def list() {
    def result = [:]
    result.user = User.get(springSecurityService.principal.id)
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
    result.subscriptionInstanceList=Subscription.list(params)
    result.subscriptionInstanceTotal=Subscription.count()
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def detail() {
    def result = [:]
    result.user = User.get(springSecurityService.principal.id)
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
    result.subscriptionInstanceList=Subscription.list(params)
    result.subscriptionInstanceTotal=Subscription.count()
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def compare() {
    def result = [:]
    result.user = User.get(springSecurityService.principal.id)
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
    result.subscriptionInstanceList=Subscription.list(params)
    result.subscriptionInstanceTotal=Subscription.count()
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def publics() {
    def result = [:]
    result.user = User.get(springSecurityService.principal.id)
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
    result.subscriptionInstanceList=Subscription.list(params)
    result.subscriptionInstanceTotal=Subscription.count()
    result
  }

}
