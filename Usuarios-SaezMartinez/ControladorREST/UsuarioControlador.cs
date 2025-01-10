using Microsoft.AspNetCore.Mvc;
using usuarios.Models;
using usuarios.Services;

namespace usuarios.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    [Produces("application/json")] // Todas las respuestas son JSON
    public class UsuariosController : ControllerBase
    {
        private readonly ServicioUsuarios _servicioUsuarios;

        public UsuariosController()
        {
            _servicioUsuarios = new ServicioUsuarios();
        }

        /// <summary>
        /// Solicitar un código de activación para un usuario.
        /// </summary>
        /// <param name="idUsuario">El ID del usuario para el cual se solicita el código de activación.</param>
        /// <returns>Un código de activación.</returns>
        [HttpPost("solicitar-codigo")]
        [ProducesResponseType(200, Type = typeof(CodigoActivacionResponse))]
        public async Task<IActionResult> SolicitarCodigoActivacion([FromQuery] string idUsuario)
        {
            var codigo = await _servicioUsuarios.SolicitarCodigoActivacion(idUsuario);
            return Ok(new CodigoActivacionResponse { Codigo = codigo });
        }

        /// <summary>
        /// Registrar un nuevo usuario con usuario/contraseña.
        /// </summary>
        /// <param name="idUsuario">ID del usuario asignado por el gestor.</param>
        /// <param name="codigoActivacion">Código de activación generado previamente.</param>
        /// <param name="request">Datos del usuario.</param>
        /// <returns>Resultado de la operación.</returns>
        [HttpPost("alta-usuario")]
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
                return Ok(new ResultadoResponse { Mensaje = "Usuario creado exitosamente." });
            }

            return BadRequest(new ResultadoResponse { Mensaje = "Error al crear el usuario. Verifique el ID y el código de activación." });
        }


        /// <summary>
        /// Registrar un nuevo usuario con OAuth2.
        /// </summary>
        /// <param name="idUsuario">ID del usuario asignado por el gestor.</param>
        /// <param name="codigoActivacion">Código de activación generado previamente.</param>
        /// <param name="request">Datos del usuario.</param>
        /// <returns>Resultado de la operación.</returns>
        [HttpPost("alta-usuario-oauth2")]
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
                return Ok(new ResultadoResponse { Mensaje = "Usuario creado exitosamente." });
            }

            return BadRequest(new ResultadoResponse { Mensaje = "Error al crear el usuario. Verifique el ID y el código de activación." });
        }


        /// <summary>
        /// Eliminar un usuario por ID.
        /// </summary>
        /// <param name="idUsuario">El ID del usuario.</param>
        /// <returns>Resultado de la operación.</returns>
        [HttpDelete("baja-usuario/{idUsuario}")]
        [ProducesResponseType(200, Type = typeof(ResultadoResponse))]
        [ProducesResponseType(404, Type = typeof(ResultadoResponse))]
        public async Task<IActionResult> BajaUsuario(string idUsuario)
        {
            var resultado = await _servicioUsuarios.BajaUsuario(idUsuario);
            if (resultado)
            {
                return Ok(new ResultadoResponse { Mensaje = "Usuario eliminado exitosamente." });
            }

            return NotFound(new ResultadoResponse { Mensaje = "Usuario no encontrado." });
        }

        /// <summary>
        /// Verificar credenciales de usuario/contraseña.
        /// </summary>
        /// <param name="request">Credenciales del usuario.</param>
        /// <returns>Mapa de claims del usuario.</returns>
        [HttpPost("verificar-credenciales")]
        [ProducesResponseType(200, Type = typeof(IEnumerable<object>))]
        [ProducesResponseType(401, Type = typeof(ResultadoResponse))]
        public async Task<IActionResult> VerificarCredenciales([FromBody] CredencialesRequest request)
        {
            var claims = await _servicioUsuarios.VerificarCredenciales(request.NombreUsuario, request.Contrasena);
            if (claims != null)
            {
                // Convertir claims a un formato legible como JSON
                var response = claims.Select(c => new { c.Type, c.Value });
                return Ok(response);
            }

            return Unauthorized(new ResultadoResponse { Mensaje = "Credenciales inválidas." });
        }


        /// <summary>
        /// Verificar usuario mediante OAuth2.
        /// </summary>
        /// <param name="request">Identificador OAuth2 del usuario.</param>
        /// <returns>Mapa de claims del usuario.</returns>
        [HttpPost("verificar-usuario-oauth2")]
        [ProducesResponseType(200, Type = typeof(IEnumerable<object>))]
        [ProducesResponseType(404, Type = typeof(ResultadoResponse))]
        public async Task<IActionResult> VerificarUsuarioOAuth2([FromBody] OAuth2Request request)
        {
            var claims = await _servicioUsuarios.VerificarUsuarioOAuth2(request.IdentificadorOAuth2);
            if (claims != null)
            {
                // Convertir claims a un formato legible como JSON
                var response = claims.Select(c => new { c.Type, c.Value });
                return Ok(response);
            }

            return NotFound(new ResultadoResponse { Mensaje = "Usuario no encontrado." });
        }


        /// <summary>
        /// Listar todos los usuarios registrados.
        /// </summary>
        /// <returns>Lista de usuarios.</returns>
        [HttpGet("listar-usuarios")]
        [ProducesResponseType(200, Type = typeof(IEnumerable<Usuario>))]
        public async Task<IActionResult> ListarUsuarios()
        {
            var usuarios = await _servicioUsuarios.ListarUsuarios();
            return Ok(usuarios);
        }
    }

    // Clases auxiliares para las respuestas
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
