package com.praticasolucoes.controle.financas.api

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(features = ["src/test/resources/features"])
class CucumberTests