/*
 * Copyright 2017 The Mifos Initiative.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mifos.rhythm.service.internal.command.handler;

import io.mifos.core.command.annotation.Aggregate;
import io.mifos.core.command.annotation.CommandHandler;
import io.mifos.core.command.annotation.CommandLogLevel;
import io.mifos.core.command.annotation.EventEmitter;
import io.mifos.core.mariadb.domain.FlywayFactoryBean;
import io.mifos.rhythm.api.v1.events.EventConstants;
import io.mifos.rhythm.service.internal.command.InitializeServiceCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * @author Myrle Krantz
 */
@SuppressWarnings("unused")
@Aggregate
public class InitializeCommandHandler {

  private final DataSource dataSource;
  private final FlywayFactoryBean flywayFactoryBean;

  @SuppressWarnings("SpringJavaAutowiringInspection")
  @Autowired
  public InitializeCommandHandler(final DataSource dataSource,
                                  final FlywayFactoryBean flywayFactoryBean) {
    super();
    this.dataSource = dataSource;
    this.flywayFactoryBean = flywayFactoryBean;
  }

  @CommandHandler(logStart = CommandLogLevel.INFO, logFinish = CommandLogLevel.INFO)
  @Transactional
  @EventEmitter(selectorName = EventConstants.SELECTOR_NAME, selectorValue = EventConstants.INITIALIZE)
  public String initialize(final InitializeServiceCommand initializeServiceCommand) {
    this.flywayFactoryBean.create(this.dataSource).migrate();
    return EventConstants.INITIALIZE;
  }
}
