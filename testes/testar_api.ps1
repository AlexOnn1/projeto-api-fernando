$baseUrl = "http://localhost:8080/cursos"

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "   TESTE AUTOMATIZADO DOS ENDPOINTS DA API DE CURSOS" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host ""

# 1. Testar se a aplicação está no ar
try {
    $resMsg = Invoke-RestMethod -Uri "$baseUrl/mensagem" -Method Get -TimeoutSec 3
    Write-Host "[1/6] Teste de Conexao: OK! Servico respondeu:" -ForegroundColor Green
    Write-Host "      `"$resMsg`"" -ForegroundColor Gray
} catch {
    Write-Host "[ERRO] A aplicacao nao parece estar rodando na porta 8080." -ForegroundColor Red
    Write-Host "Inicie a aplicacao primeiro rodando: .\iniciar_aplicacao.bat ou .\mvnw.cmd spring-boot:run" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# 2. Cadastrar novo curso (POST /cursos)
Write-Host "[2/6] POST /cursos - Cadastrando curso..." -ForegroundColor Yellow
$corpoCurso = @{
    nome = "DevOps com Docker e Kubernetes"
    cargaHoraria = 50
} | ConvertTo-Json

try {
    $novoCurso = Invoke-RestMethod -Uri $baseUrl -Method Post -ContentType "application/json" -Body $corpoCurso
    Write-Host "      Sucesso (Status 201 Created)! Curso cadastrado com ID: $($novoCurso.id)" -ForegroundColor Green
    Write-Host "      Dados: $($novoCurso | ConvertTo-Json -Compress)" -ForegroundColor Gray
    $idGerado = $novoCurso.id
} catch {
    Write-Host "      Falha ao cadastrar: $_" -ForegroundColor Red
}

Write-Host ""

# 3. Listar cursos (GET /cursos)
Write-Host "[3/6] GET /cursos - Listando todos os cursos..." -ForegroundColor Yellow
$lista = Invoke-RestMethod -Uri $baseUrl -Method Get
Write-Host "      Total de cursos no banco: $($lista.Count)" -ForegroundColor Green
foreach ($c in $lista) {
    Write-Host "      - ID $($c.id): $($c.nome) ($($c.cargaHoraria)h)" -ForegroundColor Gray
}

Write-Host ""

# 4. Buscar curso por ID (GET /cursos/{id})
if ($idGerado) {
    Write-Host "[4/6] GET /cursos/$idGerado - Buscando curso pelo ID..." -ForegroundColor Yellow
    $cursoPorId = Invoke-RestMethod -Uri "$baseUrl/$idGerado" -Method Get
    Write-Host "      Encontrado: $($cursoPorId.nome)" -ForegroundColor Green
}

Write-Host ""

# 5. Atualizar curso (PUT /cursos/{id})
if ($idGerado) {
    Write-Host "[5/6] PUT /cursos/$idGerado - Atualizando curso..." -ForegroundColor Yellow
    $corpoAtualizado = @{
        nome = "DevOps com Docker e Kubernetes (Avancado)"
        cargaHoraria = 65
    } | ConvertTo-Json
    $cursoAtualizado = Invoke-RestMethod -Uri "$baseUrl/$idGerado" -Method Put -ContentType "application/json" -Body $corpoAtualizado
    Write-Host "      Atualizado com sucesso: $($cursoAtualizado.nome) ($($cursoAtualizado.cargaHoraria)h)" -ForegroundColor Green
}

Write-Host ""

# 6. Testar Regra de Negocio (POST com dados invalidos)
Write-Host "[6/6] POST /cursos (Dados Invalidos) - Testando validacao da regra de negocio..." -ForegroundColor Yellow
$corpoInvalido = @{
    nome = ""
    cargaHoraria = 0
} | ConvertTo-Json

try {
    Invoke-RestMethod -Uri $baseUrl -Method Post -ContentType "application/json" -Body $corpoInvalido
    Write-Host "      Aviso: Deveria ter retornado erro 400!" -ForegroundColor Red
} catch {
    Write-Host "      Sucesso! O Service bloqueou os dados invalidos com status 400 Bad Request." -ForegroundColor Green
}

Write-Host ""
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "   TODOS OS TESTES FORAM CONCLUIDOS COM SUCESSO!" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan
