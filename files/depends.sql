SELECT distinct ccu.table_name,
       rc.constraint_schema||'.'||tc.table_name AS table_name,
       match_option,
       update_rule,
       delete_rule,
       kcu.column_name
FROM information_schema.referential_constraints AS rc
    JOIN information_schema.table_constraints AS tc USING(constraint_catalog,constraint_schema,constraint_name)
    JOIN information_schema.key_column_usage AS kcu USING(constraint_catalog,constraint_schema,constraint_name)
    JOIN information_schema.key_column_usage AS ccu ON(ccu.constraint_catalog=rc.unique_constraint_catalog AND ccu.constraint_schema=rc.unique_constraint_schema AND ccu.constraint_name=rc.unique_constraint_name)
WHERE ccu.table_schema='aise'
    AND ccu.table_name  in ('bairro','cidade','tipolograduro','logradouro','pessoa','cgato')
order by 1, 2

table_name     | table_name                          | match_option | update_rule | delete_rule | column_name           
---------------+-------------------------------------+--------------+-------------+-------------+-----------------------
bairro         | aise.cep                            | NONE         | NO ACTION   | SET NULL    | bairro                
bairro         | aise.cep                            | NONE         | NO ACTION   | SET NULL    | cidade                
bairro         | aise.enderecopessoa                 | NONE         | NO ACTION   | NO ACTION   | bairro                
bairro         | aise.enderecopessoa                 | NONE         | NO ACTION   | NO ACTION   | cidade                
bairro         | aise.entidade                       | NONE         | RESTRICT    | RESTRICT    | bairro                
bairro         | aise.entidade                       | NONE         | RESTRICT    | RESTRICT    | cidade                
bairro         | aise.issprecadastrobairro           | NONE         | RESTRICT    | RESTRICT    | bai_cidade            
bairro         | aise.issprecadastrobairro           | NONE         | RESTRICT    | RESTRICT    | bai_idbairro          
bairro         | aise.rntespaco                      | NONE         | RESTRICT    | RESTRICT    | bairro                
bairro         | aise.rntespaco                      | NONE         | RESTRICT    | RESTRICT    | cidade                
bairro         | aise.tribcadastrogeral              | NONE         | RESTRICT    | RESTRICT    | bairro                
bairro         | aise.tribcadastrogeral              | NONE         | RESTRICT    | RESTRICT    | cidade                
bairro         | aise.tribcadastrogeralendereco      | NONE         | RESTRICT    | RESTRICT    | bairro                
bairro         | aise.tribcadastrogeralendereco      | NONE         | RESTRICT    | RESTRICT    | cidade                
bairro         | aise.tribloteamento                 | NONE         | RESTRICT    | RESTRICT    | bairro                
bairro         | aise.tribloteamento                 | NONE         | RESTRICT    | RESTRICT    | cidade                
bairro         | aise.tribsolicitacaocontratopessoa  | NONE         | RESTRICT    | RESTRICT    | bairro                
bairro         | aise.tribsolicitacaocontratopessoa  | NONE         | RESTRICT    | RESTRICT    | cidade                
bairro         | aise.tribvistoria                   | NONE         | RESTRICT    | RESTRICT    | bairro                
bairro         | aise.tribvistoria                   | NONE         | RESTRICT    | RESTRICT    | cidade                

cgato          | aise.cgato                          | NONE         | NO ACTION   | NO ACTION   | entidadeleiautor      
cgato          | aise.cgato                          | NONE         | NO ACTION   | NO ACTION   | leiautorizativa       
cgato          | aise.cgatorevogado                  | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.cgatorevogado                  | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.cgatorevogado                  | NONE         | NO ACTION   | NO ACTION   | idatorevogado         
cgato          | aise.cgatoveiculopublicacao         | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.cgatoveiculopublicacao         | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.contconvenio                   | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.contconvenio                   | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.contconvenioaditivo            | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.contconvenioaditivo            | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.contdecreto                    | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.contdecreto                    | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.contdiaria                     | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.contdiaria                     | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.issincentivofiscal             | NONE         | RESTRICT    | RESTRICT    | idunicoatoiss         
cgato          | aise.issnotafiscal                  | NONE         | RESTRICT    | RESTRICT    | idleiincentivo        
cgato          | aise.logradourohistorico            | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.logradourohistorico            | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.obraordemplanilhaorcamento     | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.obraordemplanilhaorcamento     | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhavaliacaomodelo              | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhavaliacaomodelo              | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhcomissao                     | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhcomissao                     | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhcomissaoato                  | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhcomissaoato                  | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhconsignadofuncionario        | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhconsignadofuncionario        | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhcontratoprazodeterminado     | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhcontratoprazodeterminado     | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhcontratoprazodeterminado     | NONE         | NO ACTION   | NO ACTION   | idatorenovacao        
cgato          | aise.rhempregoanterior              | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhempregoanterior              | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhestagiario                   | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhestagiario                   | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rheventocalculo                | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rheventocalculo                | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhmovimentacaocargo            | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhmovimentacaocargo            | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhmovimentacaolegal            | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhmovimentacaolegal            | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhmovimentacaolegal            | NONE         | NO ACTION   | NO ACTION   | idatoparecer          
cgato          | aise.rhmovimentacaopessoal          | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhmovimentacaopessoal          | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhplantaocompetencia           | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhplantaocompetencia           | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhprevidencia                  | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhprevidencia                  | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhprevidencia                  | NONE         | NO ACTION   | NO ACTION   | idatoextincao         
cgato          | aise.rhprodutividadecompetencia     | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhprodutividadecompetencia     | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhprogressaorotinafuncionario  | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhprogressaorotinafuncionario  | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhquadrosalarialato            | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhquadrosalarialato            | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.rhrotinacoletiva               | NONE         | NO ACTION   | NO ACTION   | entidade              
cgato          | aise.rhrotinacoletiva               | NONE         | NO ACTION   | NO ACTION   | idato                 
cgato          | aise.tribacaofiscalparecerato       | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribacaofiscalparecerato       | NONE         | RESTRICT    | RESTRICT    | idato                 
cgato          | aise.tribaprovacaoprocesso          | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribaprovacaoprocesso          | NONE         | RESTRICT    | RESTRICT    | idato                 
cgato          | aise.tribartigo                     | NONE         | RESTRICT    | RESTRICT    | idlei                 
cgato          | aise.tribcadastroimobgeminada       | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribcadastroimobgeminada       | NONE         | RESTRICT    | RESTRICT    | numerolei             
cgato          | aise.tribcancelamentodebito         | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribcancelamentodebito         | NONE         | RESTRICT    | RESTRICT    | idato                 
cgato          | aise.tribcaucao                     | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribcaucao                     | NONE         | RESTRICT    | RESTRICT    | idato                 
cgato          | aise.tribcondominio                 | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribcondominio                 | NONE         | RESTRICT    | RESTRICT    | idato                 
cgato          | aise.tribcontencioso                | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribcontencioso                | NONE         | RESTRICT    | RESTRICT    | fundamentolegal       
cgato          | aise.tribcreditocontribuinte        | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribcreditocontribuinte        | NONE         | RESTRICT    | RESTRICT    | idato                 
cgato          | aise.tribdebito                     | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribdebito                     | NONE         | RESTRICT    | RESTRICT    | leiparcelamento       
cgato          | aise.tribdesapropriacao             | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribdesapropriacao             | NONE         | RESTRICT    | RESTRICT    | idato                 
cgato          | aise.tribfiscalpenalidade           | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribfiscalpenalidade           | NONE         | RESTRICT    | RESTRICT    | idato                 
cgato          | aise.tribfundamentolegal            | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribfundamentolegal            | NONE         | RESTRICT    | RESTRICT    | fundamentolegal       
cgato          | aise.tribguiarecolhimento           | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribguiarecolhimento           | NONE         | RESTRICT    | RESTRICT    | fundamentolegal       
cgato          | aise.tribisencao                    | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribisencao                    | NONE         | RESTRICT    | RESTRICT    | idato                 
cgato          | aise.triblivroregistro              | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.triblivroregistro              | NONE         | RESTRICT    | RESTRICT    | fundamentolegal       
cgato          | aise.tribloteamento                 | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribloteamento                 | NONE         | RESTRICT    | RESTRICT    | idato                 
cgato          | aise.tribmelhoriaedital             | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribmelhoriaedital             | NONE         | RESTRICT    | RESTRICT    | numeroedital          
cgato          | aise.tribmotivodesconto             | NONE         | RESTRICT    | RESTRICT    | entidade              
cgato          | aise.tribmotivodesconto             | NONE         | RESTRICT    | RESTRICT    | idato                 

cidade         | aise.agencia                        | NONE         | NO ACTION   | NO ACTION   | cidade                
cidade         | aise.bairro                         | NONE         | NO ACTION   | NO ACTION   | cidade                
cidade         | aise.cep                            | NONE         | NO ACTION   | NO ACTION   | cidade                
cidade         | aise.enderecopessoa                 | NONE         | NO ACTION   | NO ACTION   | cidade                
cidade         | aise.entidade                       | NONE         | NO ACTION   | NO ACTION   | cidade                
cidade         | aise.issnotafiscal                  | NONE         | RESTRICT    | RESTRICT    | intermediarioidcidade 
cidade         | aise.issnotafiscal                  | NONE         | RESTRICT    | RESTRICT    | localprestacaoidcidade
cidade         | aise.issnotafiscaltomador           | NONE         | RESTRICT    | RESTRICT    | idcidade              
cidade         | aise.issprecadastrobairro           | NONE         | RESTRICT    | RESTRICT    | bai_cidade            
cidade         | aise.issprecadastrologradouro       | NONE         | RESTRICT    | RESTRICT    | log_cidade            
cidade         | aise.issrps                         | NONE         | RESTRICT    | RESTRICT    | idcidadeservico       
cidade         | aise.issrps                         | NONE         | RESTRICT    | RESTRICT    | idmunicipioincidencia 
cidade         | aise.issrps                         | NONE         | RESTRICT    | RESTRICT    | intermediariocidade   
cidade         | aise.issrps                         | NONE         | RESTRICT    | RESTRICT    | tomadoridcidade       
cidade         | aise.localidade                     | NONE         | NO ACTION   | NO ACTION   | cidade                
cidade         | aise.logradouro                     | NONE         | NO ACTION   | NO ACTION   | cidade                
cidade         | aise.pjcomarca                      | NONE         | RESTRICT    | RESTRICT    | cidade                
cidade         | aise.rhpessoa                       | NONE         | NO ACTION   | NO ACTION   | cidadenascimento      
cidade         | aise.rhpessoa                       | NONE         | NO ACTION   | NO ACTION   | cidadetitulo          
cidade         | aise.rhprocessojudicial             | NONE         | NO ACTION   | NO ACTION   | cidadesecaojudiciaria 
cidade         | aise.tribcadastrogeralendereco      | NONE         | RESTRICT    | RESTRICT    | cidade                

logradouro     | aise.cep                            | NONE         | NO ACTION   | SET NULL    | cidade                
logradouro     | aise.cep                            | NONE         | NO ACTION   | SET NULL    | logradouro            
logradouro     | aise.enderecopessoa                 | NONE         | NO ACTION   | NO ACTION   | cidade                
logradouro     | aise.enderecopessoa                 | NONE         | NO ACTION   | NO ACTION   | logradouro            
logradouro     | aise.entidade                       | NONE         | RESTRICT    | RESTRICT    | cidade                
logradouro     | aise.entidade                       | NONE         | RESTRICT    | RESTRICT    | logradouro            
logradouro     | aise.issprecadastrologradouro       | NONE         | RESTRICT    | RESTRICT    | log_cidade            
logradouro     | aise.issprecadastrologradouro       | NONE         | RESTRICT    | RESTRICT    | log_idlogradouro      
logradouro     | aise.logradourohistorico            | NONE         | NO ACTION   | NO ACTION   | cidade                
logradouro     | aise.logradourohistorico            | NONE         | NO ACTION   | NO ACTION   | logradouro            
logradouro     | aise.rntespaco                      | NONE         | RESTRICT    | RESTRICT    | cidade                
logradouro     | aise.rntespaco                      | NONE         | RESTRICT    | RESTRICT    | logradouro            
logradouro     | aise.tribcadastrogeral              | NONE         | RESTRICT    | RESTRICT    | cidade                
logradouro     | aise.tribcadastrogeral              | NONE         | RESTRICT    | RESTRICT    | logradouro            
logradouro     | aise.tribcadastrogeralendereco      | NONE         | RESTRICT    | RESTRICT    | cidade                
logradouro     | aise.tribcadastrogeralendereco      | NONE         | RESTRICT    | RESTRICT    | logradouro            
logradouro     | aise.tribimobiliariotestadagenerica | NONE         | RESTRICT    | RESTRICT    | cidade                
logradouro     | aise.tribimobiliariotestadagenerica | NONE         | RESTRICT    | RESTRICT    | logradouro            
logradouro     | aise.triblogradourotestada          | NONE         | RESTRICT    | RESTRICT    | cidade                
logradouro     | aise.triblogradourotestada          | NONE         | RESTRICT    | RESTRICT    | delimitacaofinal      
logradouro     | aise.triblogradourotestada          | NONE         | RESTRICT    | RESTRICT    | delimitacaoinicial    
logradouro     | aise.triblogradourotestada          | NONE         | RESTRICT    | RESTRICT    | logradouro            
logradouro     | aise.triblogradourotrecho           | NONE         | RESTRICT    | RESTRICT    | cidade                
logradouro     | aise.triblogradourotrecho           | NONE         | RESTRICT    | RESTRICT    | logradouro            
logradouro     | aise.tribsolicitacaocontratopessoa  | NONE         | RESTRICT    | RESTRICT    | cidade                
logradouro     | aise.tribsolicitacaocontratopessoa  | NONE         | RESTRICT    | RESTRICT    | logradouro            
logradouro     | aise.tribtrecho                     | NONE         | RESTRICT    | RESTRICT    | cidade                
logradouro     | aise.tribtrecho                     | NONE         | RESTRICT    | RESTRICT    | logradouro            
logradouro     | aise.tribvistoria                   | NONE         | RESTRICT    | RESTRICT    | cidade                
logradouro     | aise.tribvistoria                   | NONE         | RESTRICT    | RESTRICT    | logradouro            

pessoa         | aise.banco                          | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.bancopessoa                    | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.cgagendaconfig                 | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.ciresponsavelsetor             | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.contatopessoa                  | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.contempenho                    | NONE         | NO ACTION   | NO ACTION   | fornecedor            
pessoa         | aise.contextraorcamentario          | NONE         | NO ACTION   | NO ACTION   | fornecedor            
pessoa         | aise.contfornecedorplano            | NONE         | NO ACTION   | NO ACTION   | fornecedor            
pessoa         | aise.empresaprofissional            | NONE         | NO ACTION   | NO ACTION   | pessoaempresa         
pessoa         | aise.enderecopessoa                 | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.estestoque                     | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.estmovimentacao                | NONE         | NO ACTION   | SET NULL    | pessoa                
pessoa         | aise.estrangeiro                    | NONE         | NO ACTION   | NO ACTION   | estrangeiro           
pessoa         | aise.frtabastecimento               | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.frtagendamento                 | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.frtbateria                     | NONE         | NO ACTION   | NO ACTION   | fornecedor            
pessoa         | aise.frtbombacombustivel            | NONE         | NO ACTION   | NO ACTION   | fornecedor            
pessoa         | aise.frtbombacombustivel            | NONE         | NO ACTION   | NO ACTION   | responsavel           
pessoa         | aise.frtentradacomb                 | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.frtgasto                       | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.frtmovimentacaoespecial        | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.frtocorrenciamotorista         | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.frtordemservico                | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.frtpneu                        | NONE         | NO ACTION   | NO ACTION   | fornecedor            
pessoa         | aise.frtrequisicao                  | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.imagem                         | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.issdocumentofiscal             | NONE         | RESTRICT    | RESTRICT    | pessoagrafica         
pessoa         | aise.issdocumentofiscal             | NONE         | RESTRICT    | RESTRICT    | pessoasolicitante     
pessoa         | aise.issfiscal                      | NONE         | RESTRICT    | RESTRICT    | cgcm                  
pessoa         | aise.issprecadastro                 | NONE         | RESTRICT    | RESTRICT    | pes_pessoa            
pessoa         | aise.liberacaopessoa                | NONE         | NO ACTION   | CASCADE     | pessoa                
pessoa         | aise.obraintervencao                | NONE         | RESTRICT    | RESTRICT    | construtora           
pessoa         | aise.obraintervencao                | NONE         | RESTRICT    | RESTRICT    | engenheiro            
pessoa         | aise.obraintervencao                | NONE         | RESTRICT    | RESTRICT    | responsavel           
pessoa         | aise.obranotificacao                | NONE         | NO ACTION   | NO ACTION   | fornecedor            
pessoa         | aise.obraordemplanilhaorcamento     | NONE         | NO ACTION   | NO ACTION   | responsavel           
pessoa         | aise.patbaixa                       | NONE         | NO ACTION   | NO ACTION   | comprador             
pessoa         | aise.patlocalizacao                 | NONE         | NO ACTION   | SET NULL    | responsavel           
pessoa         | aise.patmudancaresponsavel          | NONE         | NO ACTION   | NO ACTION   | responsavelanterior   
pessoa         | aise.patmudancaresponsavel          | NONE         | NO ACTION   | NO ACTION   | responsavelatual      
pessoa         | aise.patseguro                      | NONE         | NO ACTION   | NO ACTION   | corretora             
pessoa         | aise.pessoadocumento                | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.pessoahistorico                | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.pessoaramoatividade            | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.pjprocessoautorreu             | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.pjprocurador                   | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.profissional                   | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.ptcprocesso                    | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.rhagenteintegracao             | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhautonomoprevidencia          | NONE         | NO ACTION   | NO ACTION   | pessoajuridica        
pessoa         | aise.rhavaliacaoconcurso            | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhavaliacaoconcursoitem        | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhbeneficiario                 | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhconcursocandidato            | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhconcursocomissao             | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhempresatransportecoletivo    | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhempresavalealimentacao       | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhemprestimo                   | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhestagiario                   | NONE         | NO ACTION   | NO ACTION   | coordenadorcurso      
pessoa         | aise.rhestagiario                   | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhestagiariovaletransporte     | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhfornecedor                   | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhfornecedor                   | NONE         | NO ACTION   | NO ACTION   | pessoaintegracao      
pessoa         | aise.rhfuncionario                  | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhhistoricoestagiario          | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhlotacao                      | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhmovimentacaoafastamento      | NONE         | NO ACTION   | NO ACTION   | medico                
pessoa         | aise.rhmovimentacaoafastamento      | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhmovimentacaolegal            | NONE         | NO ACTION   | NO ACTION   | origempensao          
pessoa         | aise.rhmovimentacaolegal            | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhmovimentacaooutros           | NONE         | NO ACTION   | NO ACTION   | pessoajuridica        
pessoa         | aise.rhmovimentacaosaude            | NONE         | NO ACTION   | NO ACTION   | medico                
pessoa         | aise.rhmovimentacaosaude            | NONE         | NO ACTION   | NO ACTION   | pessoamonitor         
pessoa         | aise.rhpessoa                       | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhpessoahabilidade             | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhprevidencia                  | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhprocessojudicial             | NONE         | RESTRICT    | RESTRICT    | autoracao             
pessoa         | aise.rhrequisicaoportalrhtramite    | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhstprofissionalsesmt          | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.rhvalorprevidenciaaporte       | NONE         | NO ACTION   | NO ACTION   | credor                
pessoa         | aise.rntreserva                     | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.smcampanhapessoa               | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.smcampanhapessoavacina         | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.smpessoavacina                 | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.smprontuario                   | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.tribacaofiscalauditor          | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribacaofiscallevcontratosoc   | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribacaofiscalparecer          | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribagilizaobraslogin          | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribaidf                       | NONE         | RESTRICT    | RESTRICT    | pessoasolicitante     
pessoa         | aise.tribalienacaobenspessoa        | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribatendimento                | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribbloqueto                   | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribcadastrogeral              | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribcadastromobiliario         | NONE         | NO ACTION   | NO ACTION   | pessoaempresa         
pessoa         | aise.tribcobrancaavulsaos           | NONE         | NO ACTION   | NO ACTION   | pessoaexecucao        
pessoa         | aise.tribcomunicadocadastro         | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribcontratosocialpessoa       | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.tribdocumento                  | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribdocumentocadastro          | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribfiscal                     | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribimobiliaria                | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribimobiliarioitbi            | NONE         | NO ACTION   | NO ACTION   | pessoacomprador       
pessoa         | aise.tribimobiliarioitbi            | NONE         | NO ACTION   | NO ACTION   | pessoavendedor        
pessoa         | aise.triblivroregistro              | NONE         | RESTRICT    | RESTRICT    | pessoacontribuinte    
pessoa         | aise.triblivroregistropessoa        | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribloteamento                 | NONE         | RESTRICT    | RESTRICT    | loteador              
pessoa         | aise.tribloteamento                 | NONE         | RESTRICT    | RESTRICT    | proprietario          
pessoa         | aise.tribmovimentacao               | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribnotafiscalavulsa           | NONE         | RESTRICT    | RESTRICT    | pessoacliente         
pessoa         | aise.tribnotafiscalavulsa           | NONE         | RESTRICT    | RESTRICT    | pessoacontribuinte    
pessoa         | aise.tribnotificacaofiscalauditor   | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribparcelamento               | NONE         | RESTRICT    | SET NULL    | requerente            
pessoa         | aise.tribparecerista                | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribpessoacontador             | NONE         | NO ACTION   | NO ACTION   | pessoacontador        
pessoa         | aise.tribpessoaparecerista          | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.tribprojeto                    | NONE         | RESTRICT    | RESTRICT    | autorprojeto          
pessoa         | aise.tribprojeto                    | NONE         | RESTRICT    | RESTRICT    | empresaprojeto        
pessoa         | aise.tribprojeto                    | NONE         | RESTRICT    | RESTRICT    | tecnicoresponsavel    
pessoa         | aise.tribprojetoresponsavel         | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribproprietario               | NONE         | NO ACTION   | NO ACTION   | pessoa                
pessoa         | aise.tribproprietarioitbi           | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribreparcelamentoagrupado     | NONE         | RESTRICT    | RESTRICT    | requerente            
pessoa         | aise.tribrepresentantelegal         | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribrequerimento               | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribrequerimentoitbi           | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribrestricaoalvara            | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribsolicitacaocontratopessoa  | NONE         | RESTRICT    | RESTRICT    | idpessoa              
pessoa         | aise.tribsupersimples               | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribsupersimplesoficialcnpj    | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribsupersimplessoliccnpj      | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribsupersimplessolicsituacao  | NONE         | RESTRICT    | RESTRICT    | pessoa                
pessoa         | aise.tribveiculo                    | NONE         | RESTRICT    | RESTRICT    | responsavel           
pessoa         | aise.usuario                        | NONE         | NO ACTION   | SET NULL    | pessoa                

tipologradouro | aise.issprecadastrologradouro       | NONE         | RESTRICT    | RESTRICT    | log_tipologradouro    
tipologradouro | aise.logradouro                     | NONE         | NO ACTION   | SET NULL    | tipologradouro        
tipologradouro | aise.obrabem                        | NONE         | NO ACTION   | NO ACTION   | tipologradouro        
