using Microsoft.AspNetCore.Mvc;
using usuarios.Models;
using usuarios.Services;

namespace usuarios.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    [Produces("application/json")] 
    public class UsuariosController : ControllerBase
    {
        private readonly ServicioUsuarios _servicioUsuarios;

        public UsuariosController()
        {
            _servicioUsuarios = new ServicioUsuarios();
        }


        [HttpPost("solicitarCodigo")]
        [ProducesResponseType(200, Type = typeof(CodigoActivacionResponse))]
        public async Task<IActionResult> SolicitarCodigoActivacion([FromQuery] string idUsuario)
        {
            var codigo = await _servicioUsuarios.SolicitarCodigoActivacion(idUsuario);
            return Ok(new CodigoActivacionResponse { Codigo = codigo });
        }


        [HttpPost("altaUsuario")]
        [ProducesResponseType(200, Type = typeof(ResultadoResponse))]
        [ProducesResponseType(400, Type = typeof(ResultadoResponse))]
        public async Task<IActionResult> AltaUsuarioConContraseña(
            [FromQuery] string idUsuario,
            [FromQuery] string codigoActivacion,
            [FromBody] UsuarioConContraseñaRequest request)
        {
            var resultado = await _servicioUsuarios.AltaUsuario(
                idUsuario,
                codigoActivacion,
                request.NombreCompleto,
                request.CorreoElectronico,
                request.Telefono,
                request.DireccionPostal,
                request.NombreUsuario,
                request.Contrasena,
                Rol.USER);

            if (resultado)
            {
                return Ok(new ResultadoResponse { Mensaje = "Usuario creado ." });
            }

            return BadRequest(new ResultadoResponse { Mensaje = "Error al crear el usuario. Compruebe el ID y el código de activación." });
        }


        [HttpPost("altaUsuario-oauth2")]
        [ProducesResponseType(200, Type = typeof(ResultadoResponse))]
        [ProducesResponseType(400, Type = typeof(ResultadoResponse))]
        public async Task<IActionResult> AltaUsuarioConOAuth2(
            [FromQuery] string idUsuario,
            [FromQuery] string codigoActivacion,
            [FromBody] UsuarioOAuth2Request request)
        {
            var resultado = await _servicioUsuarios.AltaUsuario(
                idUsuario,
                codigoActivacion,
                request.NombreCompleto,
                request.CorreoElectronico,
                request.Telefono,
                request.DireccionPostal,
                request.IdentificadorOAuth2,
                Rol.USER);

            if (resultado)
            {
                return Ok(new ResultadoResponse { Mensaje = "Usuario creado ." });
            }

            return BadRequest(new ResultadoResponse { Mensaje = "Error al crear el usuario. Compruebe el ID y el código de activación." });
        }


        [HttpDelete("bajaUsuario/{idUsuario}")]
        [ProducesResponseType(200, Type = typeof(ResultadoResponse))]
        [ProducesResponseType(404, Type = typeof(ResultadoResponse))]
        public async Task<IActionResult> BajaUsuario(string idUsuario)
        {
            var resultado = await _servicioUsuarios.BajaUsuario(idUsuario);
            if (resultado)
            {
                return Ok(new ResultadoResponse { Mensaje = "Usuario eliminado ." });
            }

            return NotFound(new ResultadoResponse { Mensaje = "Usuario no encontrado." });
        }

        [HttpPost("verificarCredenciales")]
        [ProducesResponseType(200, Type = typeof(IEnumerable<object>))]
        [ProducesResponseType(401, Type = typeof(ResultadoResponse))]
        public async Task<IActionResult> VerificarCredenciales([FromBody] CredencialesRequest request)
        {
            var claims = await _servicioUsuarios.VerificarCredenciales(request.NombreUsuario, request.Contrasena);
            if (claims != null)
            {
                var response = claims.Select(c => new { c.Type, c.Value });
                return Ok(response);
            }

            return Unauthorized(new ResultadoResponse { Mensaje = "Credenciales inválidas." });
        }


        [HttpPost("verificarUsuarioOauth2")]
        [ProducesResponseType(200, Type = typeof(IEnumerable<object>))]
        [ProducesResponseType(404, Type = typeof(ResultadoResponse))]
        public async Task<IActionResult> VerificarUsuarioOAuth2([FromBody] OAuth2Request request)
        {
            var claims = await _servicioUsuarios.VerificarUsuarioOAuth2(request.IdentificadorOAuth2);
            if (claims != null)
            {
                var response = claims.Select(c => new { c.Type, c.Value });
                return Ok(response);
            }

            return NotFound(new ResultadoResponse { Mensaje = "Usuario no encontrado." });
        }

        [HttpGet("listarUsuarios")]
        [ProducesResponseType(200, Type = typeof(IEnumerable<Usuario>))]
        public async Task<IActionResult> ListarUsuarios()
        {
            var usuarios = await _servicioUsuarios.ListarUsuarios();
            return Ok(usuarios);
        }
    }

    public class ResultadoResponse
    {
        public string Mensaje { get; set; }
    }

    public class CodigoActivacionResponse
    {
        public string Codigo { get; set; }
    }

    public class UsuarioConContraseñaRequest
    {
        public string NombreCompleto { get; set; }
        public string CorreoElectronico { get; set; }
        public string? Telefono { get; set; }
        public string? DireccionPostal { get; set; }
        public string NombreUsuario { get; set; }
        public string Contrasena { get; set; }
    }

    public class UsuarioOAuth2Request
    {
        public string NombreCompleto { get; set; }
        public string CorreoElectronico { get; set; }
        public string? Telefono { get; set; }
        public string? DireccionPostal { get; set; }
        public string IdentificadorOAuth2 { get; set; }
    }

    public class CredencialesRequest
    {
        public string NombreUsuario { get; set; }
        public string Contrasena { get; set; }
    }

    public class OAuth2Request
    {
        public string IdentificadorOAuth2 { get; set; }
    }

}
